package com.ycj.idempotent.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jackspeed
 * @date 2020/03/27
 */
@Data
@NoArgsConstructor
public class UserInfo {
    private String userNo;
    private String userName;
    private String userPwd;
    private Integer weight;
    private Integer age;

    public UserInfo(String userNo, String userName, String userPwd, Integer weight, Integer age) {
        this.userNo = userNo;
        this.userName = userName;
        this.userPwd = userPwd;
        this.weight = weight;
        this.age = age;
    }
}
