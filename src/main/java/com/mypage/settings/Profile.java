package com.mypage.settings;

import com.mypage.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class Profile {

    @Length(max = 35)
    private String bio;  //프로필 자기소개

    @Length(max = 50)
    private String url; //웹사이트url

    @Length(max = 50)
    private String occupation; //직업

    @Length(max = 50)
    private String location; //사는지역

    private String profileImage; //프로필 이지미미


   public Profile(Account account){
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
        this.profileImage =account.getProfileImage();
    }

}
