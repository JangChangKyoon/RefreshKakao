package com.example.refreshtokenandkakao.common.jwt;

import com.example.refreshtokenandkakao.common.exception.CustomException;
import com.example.refreshtokenandkakao.common.exception.ErrorCode;
import com.example.refreshtokenandkakao.user.Users;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//1. AccessToken, 비밀키 설정
//2. 토큰 생성(jwt설정 -> accessToken설정 -> TokenDto에 빌드)
//3. 토큰의 유효성 + 만료일자 확인
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    // Access Token 유효기간 - 하루
    private static final Long accessTokenValidTime = 24 * 60 * 60 * 1000L;
    //static 공유하기 위한 용도

    //Refresh Token 유효기간 - 7일
    private static final Long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));}

    // 토큰 생성
    public TokenDto createToken(Users users) {
        // JSON WEB TOKEN 클라이언트에 세션 상태를 저장하는 것이 아닌
        // 필요한 정보를 토큰 body에 저장해 클라이언트에 저장해두고 이를 증명서처럼 사용한다.

        // 1. header : jwt 토큰의 유형이나 사용된 해시알고리즘의 정보가 들어간다.
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");

        // 2. payload : 클라임을 포함한다. 즉 클라이언트에 대한 정보가 들어간다.
        //Claim based
        //Claim : 사용자에 대한 프로퍼티 / 속성
        Claims claims = Jwts.claims().setSubject(String.valueOf(users.getId()));
        claims.put("username", users.getUsername());
        claims.put("nickname", users.getNickname());


        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setIssuedAt(now) //iss(토큰발급자), sub(토큰제목_subject), aud(토큰 대상자)
                                  //exp(토큰만료시간), nbf(토큰활성날짜), iat(토큰발급된시간)
                                  //jti(jwt 고유식별자, 중복처리 방지용)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                // Signature : 서명을 통해 메시지가 중간에 변경되지 않았다는 것을 증명
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenValidTime)
                .refreshToken(refreshToken)
                .build();
    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    //servlet : Servlet은 Java를 사용하여 웹페이지를 동적으로 생성하는 서버측 프로그램
    //          웹페이지에 정보를 주고 받는 역할을 해주는 클래스이다. 지금은 헤더를 조작하는 데에 주로 쓰이며, 나머지는 스프링부트 기능으로 대체된 것 같다.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");}

    // 토큰의 유효성 + 만료일자 확인
    public JwtReturn validateToken(String jwtToken) {
        System.out.println(jwtToken);
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            if (claims.getBody().getExpiration().after(new Date())) {
                return JwtReturn.SUCCESS;
            } else {
                return JwtReturn.FAIL;
            }
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return JwtReturn.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않은 JWT 토큰입니다.");
            throw new CustomException(ErrorCode.JWT_TOKEN_NOT_SUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch(MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch(Exception e) {
            log.info(e.getMessage());
        }
        return JwtReturn.FAIL;
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
