package com.example.refreshtokenandkakao.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUsername(String memberId);

    boolean existsByNickname(String nickname);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByKakaoId(Long kakaoId);

    Optional<Users> findById(Long userId);

    Optional<Users> findByNickname(String nickname);







}
