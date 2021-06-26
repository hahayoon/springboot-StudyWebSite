package com.mypage.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  //런타임까지 유자되어야함
@Target(ElementType.PARAMETER)        //타겟은 파라미터에만 붙일수있게
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")  //AnonymousUser라면 null 아니면 ACCOUNT 전달
public @interface CurrentUser {
}
