package com.mypage.account;

import com.mypage.domain.Account;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

//shft +ctrl + t :테스트 작성


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up";
        }

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);


        return "redirect:/";

    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token , String email , Model model) {
        Account account =accountRepository.findByEmail(email); //이메일에 해당하는 유저가 있는지 확인

        String view ="account/checked-Email";

        if(account == null){
            model.addAttribute("error","wrong email");
            return view;

        }

        if(!account.isValidToken(token)){  //true가 아니면
       //f(!account.getEmailCheckToken().equals(token)){
           model.addAttribute("error","wrong email");
           return view;

       }

       account.completeSignUp();
        accountService.login(account);

       model.addAttribute("numberOfUser",accountRepository.count());
       model.addAttribute("nickname",account.getNickname());
      return view;

    }
}


