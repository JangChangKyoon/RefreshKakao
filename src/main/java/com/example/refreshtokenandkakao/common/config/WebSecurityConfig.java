package com.example.refreshtokenandkakao.common.config;

import com.example.refreshtokenandkakao.common.jwt.JwtAuthenticationFilter;
import com.example.refreshtokenandkakao.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 활성화 , 스프링 시큐리티 필터가 스프링 필터 체인에 등록
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder encoderPassword() {return new BCryptPasswordEncoder();}


    @Override
    public void configure(WebSecurity web) {
         web
                 //h2 허용(CSRF, FrameOptions 무시)
                 .ignoring()
                 .antMatchers("/h2-console/**");
        }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource());
        http.csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.authorizeRequests();


        http.authorizeRequests()
                //인가 없이 허용
                .antMatchers(HttpMethod.POST, "/user/signup/**").permitAll()
                // 그 외 어떤 요청이든 '인증'과정 필요
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:3000");
        //configuration.addAllowedOrigin(https frond 주소)
        //configuration.addAllowedOrigin((https 구매한 도메인 주소)
        //configuration.addAllowedOrigin(http 구매한 도메인 주소)
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }



//    http.csrf() 는 정상적인 사용자가 의도치 않는 위조요청을 보내는것을
//    막기 위해서 html에서 csrf 토큰이 포함되어야만 요청을 받는것이다.

//    하지만 우리는 비활성화를 하기로 하였다. 이유는 rest api에서는
//    csrf의 공격으로 부터 안전하고 매번 csrf 토큰을 받는것은 불필요하다고 생각되기 때문이다
//@Override
//protected void configure(HttpSecurity http) throws Exception {
//    http.csrf().disable(); // 비활성화
//    http.authorizeRequests()
//            .antMatchers("/user/**").authenticated() // 다음과 같은 주소는 인증이 필요
//            .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 인증뿐 아니라 권한이 있어야함
//            .anyRequest().permitAll() // 다른 것은 모두 허용
//            .and()
//            .formLogin()
//            .loginPage("/login"); // 로그인 페이지 설정
}
