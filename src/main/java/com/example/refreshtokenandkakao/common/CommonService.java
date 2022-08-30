package com.example.refreshtokenandkakao.common;

import com.example.refreshtokenandkakao.user.UserDetailsImpl;
import com.example.refreshtokenandkakao.user.UserRepository;
import com.example.refreshtokenandkakao.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonService {
    private final UserRepository userRepository;

    public Users getUser() {
        log.info("===========================1-1고승유=====================================================");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        String username = principal.getUser().getUsername();
        log.info("===========================1-2고승유=====================================================");

        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("존재하지 않는 유저입니다")
        );
    }
}

