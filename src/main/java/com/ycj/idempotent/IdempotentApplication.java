package com.ycj.idempotent;

import com.ycj.idempotent.annotations.TokenAnnotation;
import com.ycj.idempotent.constants.JConstant;
import com.ycj.idempotent.model.UserInfo;
import com.ycj.idempotent.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@SpringBootApplication
public class IdempotentApplication {

    @Autowired
    TokenService tokenService;

    public static void main(String[] args) {
        SpringApplication.run(IdempotentApplication.class, args);
    }

    @GetMapping("/login")
    public Object login(HttpServletResponse response, String userNo) {
        UserInfo userInfo = getUserInfo(userNo);
        response.setHeader(JConstant.TOKEN_NAME, tokenService.createToken(userInfo));
        return userInfo;
    }

    @TokenAnnotation
    @GetMapping("/findById/{userNo}")
    public Object findById(@PathVariable String userNo) {
        UserInfo userInfo = getUserInfo(userNo);
        return userInfo;
    }

    private UserInfo getUserInfo(String userNo) {
        return new UserInfo(userNo, "sa", "111", 68, 11);
    }
}
