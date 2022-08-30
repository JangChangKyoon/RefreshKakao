package com.example.refreshtokenandkakao.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserInfo {
    private String username;
    private String nickname;
    private String profileImg;
    private List<String> stacks;

    //public UserInfo(String username, String nickname, String profileImg, List<Stack> stacks) {
    public UserInfo(String username, String nickname, String profileImg) {
        this.username = username;
        this.nickname = nickname;
        //this.profileImg = profileImg;
        //this.stacks = listToArrayList(stacks);
    }

//    private List<String> listToArrayList(List<Stack> stackList){
//        List<String> stacks = new ArrayList<>();
//        for(Stack stack : stackList){
//            stacks.add(stack.getStack());
//        }
//        return stacks;
//    }
}

