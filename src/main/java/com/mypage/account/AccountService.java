package com.mypage.account;

import com.mypage.domain.Account;
import com.mypage.settings.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;



    private Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
               // .password(signUpForm.getPassword()) // TODO encoding 해야함
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();

        Account newAccount = accountRepository.save(account);  //accountrepository 자동 트랜젝션 extends jparepository
        return newAccount;
    }


    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage(); //이메일 보내기
        mailMessage.setSubject("스터디 올래, 회원가입 인증"); // 이메일 제목
        mailMessage.setText("/check-email-token?token="+ newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());//이메일 본문

        javaMailSender.send(mailMessage);//이메일 보냄
    }



    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);  //ctrl+ alt + m -> 드래그해서 매소드만들기
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);


        return newAccount;

    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),  //principal
               // account.getNickname(),
                account.getPassword(),   //password
                List.of(new SimpleGrantedAuthority("ROLE_USER")));  //권한

       SecurityContextHolder.getContext().setAuthentication(token);
    }



    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null){
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if(account == null){
            throw new UsernameNotFoundException(emailOrNickname);  //계정이 없으면 예외처리

        }

        return new UserAccount(account);  //계정이 있으면 계정을 넘겨줘
    }

    public void completeSignUp(Account account) {
        account.completeSignUp();
        login(account);

    }

    public void updateProfile(Account account, Profile profile) {
        account.setUrl(profile.getUrl());
        account.setOccupation(profile.getOccupation());
        account.setLocation(profile.getLocation());
        account.setBio(profile.getBio());
        account.setProfileImage(profile.getProfileImage());
        accountRepository.save(account);  // 아이디가 있으면 update를 시켜준다.
    }

    public void updataPassword(Account account, String newPassword) {
        account.setPassword(newPassword);
        accountRepository.save(account);  //detech 되어있기때문에 save 호출해서 영속성 context
    }
}





