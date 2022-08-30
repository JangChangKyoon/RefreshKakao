package com.example.refreshtokenandkakao.user;

import com.example.refreshtokenandkakao.user.dto.SignupRequestDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@DynamicUpdate
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String username;

    @Column String password;

    @Column
    private String nickname;

    @Column Long kakaoId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


    public void updateNickname(SignupRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
    }
}
