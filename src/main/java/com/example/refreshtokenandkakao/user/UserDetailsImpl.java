package com.example.refreshtokenandkakao.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


//로그인을 구현하기위해서 auth 패키지를 생성한다.
//auth 패키지는 Spring Security가 로그인을 진행한다. 그 때 로그인이 완료가 되면 Security Session을 만들어준다 ( security contextholder 안에 저장)
//이 Session 에 들어가는 정보 Object 가 정해져 있는데 이것이 바로 Authentication 객체여야 한다.
//Authentication 안에는 User 정보가 있어야 한다 -> User Object의 type은 UserDetails type 객체여야한다.
//즉 Spring Security => Authentication 객체 => UserDetails 객체
//이렇게 있다는 뜻이다.


public class UserDetailsImpl implements UserDetails {
    private final Users users;

    public UserDetailsImpl(Users users) {
        this.users = users;
    }

    public Users getUser() {
        return users;
    }

    @Override
    public String getPassword() {
        return users.getPassword();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {return true;}
    //계정이 만료되지 않았는지를 리턴한다(true를 리턴하면 만료되지 않음을 의미)

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //계정이 잠겨있지 않은지를 리턴한다(true를 리턴하면 계정이 잠겨있지 않음을 의미)


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //계정의 패스워드가 만료되지 않았는지를 리턴한다(true를 리턴하면 패스워드가 만료되지 않음을 의미)

    @Override
    public boolean isEnabled() {return true;}
    //계정이 사용가능한 계정인지를 리턴한다(true를 리턴하면 사용가능한 계정인지를 의미)
    // 사이트 내에서 1년동안 로그인을 안하면 휴먼계정을 전환을 하도록 하겠다.
    // -> loginDate 타입을 모아놨다가 이 값을 false로 return 해버리면 된다.

    //해당 User의 권한을 리턴하는곳. 계정이 갖고 있는 권한 목록을 리턴한다
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        UserRoleEnum role = user.getRole();
//        String authority = role.getAuthority();
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(simpleGrantedAuthority);



        return authorities;
    }
}
