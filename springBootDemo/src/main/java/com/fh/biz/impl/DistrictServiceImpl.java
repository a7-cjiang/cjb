package com.fh.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.biz.DistrictService;
import com.fh.mapper.DistricrMapper;
import com.fh.po.District;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    private DistricrMapper districrMapper;


    @Override
    public String getDistrict() {
        String district_ssn = RedisUse.get("district_ssn");
        if (StringUtils.isEmpty(district_ssn)){
            List<District> districts = districrMapper.selectList(null);
            String s = JSONObject.toJSONString(districts);
            RedisUse.set("district_ssn",s);
            return RedisUse.get("district_ssn");
        }
        return district_ssn;
    }
}
