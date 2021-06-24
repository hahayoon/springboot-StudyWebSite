package com.mypage.account;

import com.mypage.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired private MockMvc mockMvc; //서버에 배포하지 않고도 스프링 MVC의 동작을 재현할 수 있는 클래스
    @Autowired private AccountRepository accountRepository;
    @MockBean
    JavaMailSender javaMailSender;

    @DisplayName("회원가입 화면 보이는지 테스트 ")
    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));

    }

    @DisplayName("회원가입 처리 - 입력값 오류 ")
    @Test
    void signUpSubmit_with_long_input()throws Exception{
        mockMvc.perform(post("/sign-up")
                .param("nickname","keesun")
                .param("email","email@net")
                .param("password","12345")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));


    }


    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "keesun")
                .param("email", "keesun@email.com")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

       Account account=  accountRepository.findByEmail("keesun@email.com");
       assertNotNull(account);
       // assertTrue(accountRepository.existsByEmail("keesun@email.com"));

        assertNotEquals(account.getPassword(),"12345678"); //비밀번호가 해쉬화 되었는지 확인
        then(javaMailSender).should().send(any(SimpleMailMessage.class));


    }



}