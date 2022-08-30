package com.example.refreshtokenandkakao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // 로그인 요청이 오면 자동으로 loadUserByUsername 함수가 실행되도록 정의되어있다
    //이 때 매개변수로 받는 String username은 html에서 정의된 input type의 name 과 동일하여야 한다.
    //이렇게 찾는 Username이 있으면 유저 객체를 반환해주고 없으면 null을 반환하여 로그인을 하게 해준다
    public UserDetailsImpl loadUserByUsername(String userPk) throws UsernameNotFoundException {
        Users users = (Users)userRepository.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new UsernameNotFoundException(userPk + "은 존재하지 않은 아이디입니다."));

        return new UserDetailsImpl(users);
    }
}
