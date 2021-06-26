package com.mypage.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode (of ="id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;  //이메일 또는 닉네임을 통해서 로그인 가능

    @Column(unique = true)
    private String nickname;


    private String password;

    private boolean emailVerified;  //이메일 인증이 된 계정인지 구분

    private String emailCheckToken; //이메일 저장 토큰값

    private LocalDateTime joinAt;  //가입시간

    private String bio;  //프로필 자기소개

    private String url; //웹사이트url

    private String occupation; //직업

    private String location; //사는지역

    @Lob @Basic(fetch = FetchType.EAGER)
    private String profileImage; //FetchType.EAGER 참조객체까지 모두 읽어옴. <-> LAZY:해당 객체만 가져옴.

    private boolean studyCreatedByEmail; //알림설정 , 만들어진것을 이메일로 받을것인가

    private boolean studyCreatedByWeb; //알림설정 , 만들어진것을 웹으로 받을것인가

    private boolean studyEnrollmentResultByEmail; //가입신청 결과를 이메일로 받을것인가.

    private  boolean studyEnrollmentResultByWeb; //가입신청 결과를 이메일로 받을것인가.

    private boolean studyUpdatedByEmail; //갱신정보 이메일로 받을것인가

    private boolean studyUpdatedByWeb;  //갱신정보 웹으로 받을것인가.

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }


    public void completeSignUp() {
        this.emailVerified = true;
        this.joinAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }
}
