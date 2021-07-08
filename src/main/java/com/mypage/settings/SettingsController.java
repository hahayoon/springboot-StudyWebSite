package com.mypage.settings;

import com.mypage.account.AccountService;
import com.mypage.account.CurrentUser;
import com.mypage.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class SettingsController {


    //@Valid annotation으로 검증이 필요한 객체를 가져오기 전에 수행할 method를 지정해주는 annotation이다.
    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(new PasswordFormVaildator());
    }


    private static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
    private final AccountService accountService;

    @GetMapping("/settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new Profile(account));
        return SETTINGS_PROFILE_VIEW_NAME;
    }


    @PostMapping("/settings/profile")
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors,
                                Model model , RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }

        accountService.updateProfile(account,profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다. "); //html에 model로 전송 한번쓰고 사라짐
        return "redirect:" + "/settings/profile";
    }

    @GetMapping("/settings/password")
    public String updatePasswordForm(@CurrentUser Account account, Model model ){
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());

        return "settings/password";

    }

    @PostMapping("/settings/password")
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm,Errors errors,
                                 Model model, RedirectAttributes attributes){

        if(errors.hasErrors()){
            model.addAttribute(account);
            return "settings/password";
        }

        accountService.updataPassword(account,passwordForm.getNewPassword());
        attributes.addFlashAttribute("message","패스워드를 변경했습니다. ");
        return "redirect:"+"/settings/password";

    }}
