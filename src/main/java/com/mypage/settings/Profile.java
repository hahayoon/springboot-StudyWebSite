package com.mypage.settings;

import com.mypage.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Profile {

    private String bio;  //프로필 자기소개

    private String url; //웹사이트url

    private String occupation; //직업

    private String location; //사는지역


    public Profile(Account account){
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }

}
