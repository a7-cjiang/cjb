package com.fh.biz.impl;

import com.fh.biz.VipService;
import com.fh.mapper.VipMapper;
import com.fh.utils.JWT;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class VipServiceImpl implements VipService {

    @Autowired
    private VipMapper vipMapper;

    @Override
    public String getPhone(String phone) {
        String phone1 = vipMapper.getPhone(phone);
        return phone1;
    }

    @Override
    public Map login(String phone,String code) {
        Map map = new HashMap();
        if (phone.length()==11){//手机号登录

            String s = RedisUse.get(phone + "_ssn");
            if (s != null && s.equals(code)){

                Map user = new HashMap();
                user.put("phone",phone);

                //生成秘钥
                String sign = JWT.sign(user, 60 * 60 * 24 * 1000);
                //加签，手机号+sign 防止数据被篡改
                String token = Base64.getEncoder().encodeToString((phone + "," + sign).getBytes());
                //将秘钥放入redis，保证只有最新的能使用
                RedisUse.set("token_"+phone,sign,60*60*24);

                map.put("status",200);
                map.put("message","登录成功");
                map.put("token",token);

            }else {
                map.put("status",500);
                map.put("message","验证码无效");
            }

        }else {//用户名登录
            String userPhone = vipMapper.getPhone(phone);
            String s = RedisUse.get(userPhone + "_ssn");
            if (StringUtils.isNotBlank(s) && s.equals(code)){

                Map user = new HashMap();
                user.put("phone",userPhone);

                //生成秘钥
                String sign = JWT.sign(user, 60 * 60 * 24 * 1000);
                //
                String token = Base64.getEncoder().encodeToString((userPhone + "," + sign).getBytes());
                //
                RedisUse.set("token_"+userPhone,sign,60*60*24);

                map.put("status",200);
                map.put("message","登陆成功");
                map.put("token",token);
            }else {
                map.put("status",500);
                map.put("message","验证码无效");
            }

        }
        return map;
    }
}
