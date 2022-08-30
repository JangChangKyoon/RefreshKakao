package com.example.refreshtokenandkakao.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
//jwtProvider토큰 생성용
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
}
