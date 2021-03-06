package com.mypage.config;

import com.mypage.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//@EnableWebSecurity: 세큐리티 설정을 직접 하겠다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()); //static 은 허용.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {  //특정주소는 security 인증을안해도 들어가진다.
        http.authorizeRequests()
               .mvcMatchers("/","/login","/sign-up" ,"/check-email-token" ,"/dropdown"
                ,"/email-login", "/check-email-login" , "/login-link" ).permitAll()
                .mvcMatchers(HttpMethod.GET,"/profile/*").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login").permitAll();

        http.logout()
                .logoutSuccessUrl("/");

        http.rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository());

    }

    public PersistentTokenRepository tokenRepository(){

        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return  jdbcTokenRepository;

    }

}
