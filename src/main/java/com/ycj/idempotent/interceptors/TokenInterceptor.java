package com.ycj.idempotent.interceptors;

import com.alibaba.fastjson.JSON;
import com.ycj.idempotent.annotations.TokenAnnotation;
import com.ycj.idempotent.constants.JConstant;
import com.ycj.idempotent.enums.ResultCode;
import com.ycj.idempotent.model.UserInfo;
import com.ycj.idempotent.service.TokenService;
import com.ycj.idempotent.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author jackspeed
 * @date 2020/03/27
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenService tokenService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        TokenAnnotation annotation = method.getAnnotation(TokenAnnotation.class);
        if (annotation != null) {
            String token = request.getHeader(JConstant.TOKEN_NAME);
            if (StringUtils.isEmpty(token)) {
                HashMap<String, Object> result = new HashMap<>();
                result.put(ResultCode.NEED_TOKEN.getMsg(), ResultCode.NEED_TOKEN.getCode());
                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }
            //
            String tokenInfoStr = tokenService.getTokenInfo(token);
            if (StringUtils.isEmpty(tokenInfoStr)) {
                HashMap<String, Object> result = new HashMap<>();
                result.put(ResultCode.TOKEN_ERROR.getMsg(), ResultCode.TOKEN_ERROR.getCode());
                response.getWriter().write(JSON.toJSONString(result));
                return false;
            }
            //删除token
            tokenService.deleteToken(token);
            //用户信息json解密
            String tokenInfo = AESUtil.decryptToken(tokenInfoStr);
            //json解析
            UserInfo userInfo = JSON.parseObject(tokenInfo, UserInfo.class);
            //根据用户信息刷新token
            response.setHeader(JConstant.TOKEN_NAME, tokenService.createToken(userInfo));
            return true;
        }
        return true;
    }
}
