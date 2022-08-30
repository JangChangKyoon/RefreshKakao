package com.example.refreshtokenandkakao.common.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);

        //토큰이 비어있으면
        if (token != null && !token.isEmpty()) {
            token = token.replaceAll("Bearer","");
        }

        // 토큰이 만료되었으면
        if (token != null && jwtTokenProvider.validateToken(token) == JwtReturn.EXPIRED) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
            return;
        }
        //토큰이 실패하면
        if (token != null && jwtTokenProvider.validateToken(token) == JwtReturn.FAIL) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            return;
        }
        //토큰이 성공하면
        if(token != null && jwtTokenProvider.validateToken(token) == JwtReturn.SUCCESS) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}
