package com.fh.common.intercepter;

import com.fh.common.exception.NologinException;
import com.fh.utils.JWT;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.logging.LogException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Map;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头信息
        String token = request.getHeader("token");

        //验证请求头信息是否完整
        if (StringUtils.isEmpty(token)){
            throw new NologinException("没有登录！");
        }

        //解密字节数组
        byte[] decode = Base64.getDecoder().decode(token);
        //将字节数组转为string类型   格式：(phone + "," + sign)
        String signToken = new String(decode);

        //判断是否被篡改
        String[] split = signToken.split(",");
        if (split.length != 2){
            throw new NologinException("没有登录！");
        }

        String phone = split[0];

        //jwt秘钥
        String sign = split[1];

        Map user = JWT.unsign(sign, Map.class);

        if (user == null){
            throw new NologinException("没有登录！");
        }else {//jwt验证通过
            String sign_redis = RedisUse.get("token_" + user.get("phone"));
            if (!sign.equals(sign_redis)){//与redis中的秘钥对比，是否为最新的
                throw new NologinException("验证码失效");
            }
        }

        RedisUse.set("token_" + user.get("phone"),sign,60*60*24);

        //将用户信息放入request中，方便后面需求处理
        request.setAttribute("login_user",user);

        return true;
    }
}
