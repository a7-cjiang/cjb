package com.fh.controller;

import com.fh.biz.VipService;
import com.fh.common.JsonData;
import com.fh.utils.JWT;
import com.fh.utils.MessageUtils;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("vip")
public class VipController {

    @Autowired
    private VipService vipService;


    @GetMapping("sendMessage")
    public JsonData sendMessage(String phone){
        if (phone.length() == 11){
            //String code = MessageUtils.sendMsg(phone);
            String code = "1111";
            RedisUse.set(phone+"_ssn",code,60*60*24);
        }else {
            String usePhone = vipService.getPhone(phone);
            if (StringUtils.isBlank(usePhone)){
                return JsonData.success("用户未注册");
            }
            //String code = MessageUtils.sendMsg(usePhone);
            String code = "1111";
            RedisUse.set(usePhone+"_ssn",code,60*60*24);
        }
        return JsonData.success("发送短信成功！");
    }

    @GetMapping("login")
    public JsonData login(String phone,String code){
            Map map = vipService.login(phone,code);
        return JsonData.success(map);
    }

}
