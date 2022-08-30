package com.example.refreshtokenandkakao.user.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SignupRequestDto {
    private Long userId;
    private String username;
    private String password;
    private String passwordConfirm;
    private String nickname;
    //private List<StackDto> stacks = new ArrayList<>();
}
