package com.fh.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.biz.TypeService;
import com.fh.mapper.TypeMapper;
import com.fh.po.Type;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeMapper typeMapper;


    @Override
    public String getType() {
        String type_ssn = RedisUse.get("type_ssn");
        if (StringUtils.isEmpty(type_ssn)) {
            List<Type> list = typeMapper.selectList(null);
            String s = JSONObject.toJSONString(list);
            RedisUse.set("type_ssn",s);
            return RedisUse.get("type_ssn");
        }
        return type_ssn;

    }
}
