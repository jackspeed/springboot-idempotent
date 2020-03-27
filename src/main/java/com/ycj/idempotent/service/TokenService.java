package com.ycj.idempotent.service;

import com.alibaba.fastjson.JSON;
import com.ycj.idempotent.model.UserInfo;
import com.ycj.idempotent.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jackspeed
 * @date 2020/03/27
 */
@Service
public class TokenService {

    @Autowired
    RedisService redisService;

    public void deleteToken(String key) {
        boolean result = redisService.remove(key);
        if (!result) {
            throw new RuntimeException("set token error");
        }
    }

    public String getTokenInfo(String token) {
        Object obj = redisService.get(token);
        return obj == null ? null : obj.toString();
    }

    public String createToken(UserInfo userInfo) {
        String token = "key_" + userInfo.getUserNo() + "_" + System.nanoTime();
        String jsons = AESUtil.encryptToken(JSON.toJSONString(userInfo));
        boolean result = redisService.setEx(token, jsons, 60L);
        if (result) {
            return token;
        }
        throw new RuntimeException("set token error");
    }

}
