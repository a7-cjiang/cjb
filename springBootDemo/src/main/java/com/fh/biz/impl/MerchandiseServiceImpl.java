package com.fh.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.biz.MerchandiseService;
import com.fh.mapper.MerchandiseMapper;
import com.fh.po.Merchandise;
import com.fh.utils.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchandiseServiceImpl implements MerchandiseService {

    @Autowired
    private MerchandiseMapper merchandiseMapper;


    @Override
    public String getHotMerchandise() {
        String productHot_ssn = RedisUse.get("productHot_ssn");
        if (StringUtils.isEmpty(productHot_ssn)){
            List<Merchandise> hotProduct = merchandiseMapper.getHotProduct();
            String s = JSONObject.toJSONString(hotProduct);
            RedisUse.set("productHot_ssn",s);
            return RedisUse.get("productHot_ssn");
        }
        return productHot_ssn;
    }

    @Override
    public List<Merchandise> getProdecuBytypeId(Integer typeId) {
        List<Merchandise> prodecuBytypeId = merchandiseMapper.getProdecuBytypeId(typeId);
        return prodecuBytypeId;
    }

    @Override
    public Merchandise getProductById(Integer id) {
        Merchandise productById = merchandiseMapper.getProductById(id);
        return productById;
    }

}
