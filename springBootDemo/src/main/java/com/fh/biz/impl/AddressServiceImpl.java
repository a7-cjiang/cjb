package com.fh.biz.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.biz.AddressService;
import com.fh.mapper.AddressMapper;
import com.fh.mapper.VipMapper;
import com.fh.po.Address;
import com.fh.po.District;
import com.fh.po.ShopUserVip;
import com.fh.po.vo.AddressVo;
import com.fh.utils.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private VipMapper vipMapper;



    @Override
    public List getAddressVoList() {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        Integer vipId = 0;

        if (phone.length() == 11){
            ShopUserVip vip = vipMapper.getVipByNumber(phone);
            vipId = vip.getId();
        }else {
            ShopUserVip vip = vipMapper.getVipByName(phone);
            vipId = vip.getId();
        }

        QueryWrapper<Address> qw = new QueryWrapper<>();
        qw.eq("vipId",vipId);
        List<Address> addresses = addressMapper.selectList(qw);

        List<AddressVo> list = new ArrayList<>();

        for (int i = 0; i < addresses.size(); i++) {
            AddressVo addressVo = new AddressVo();

            Address address = addresses.get(i);

            addressVo.setId(address.getId());
            addressVo.setIsCheck(address.getIsCheck());
            addressVo.setName(address.getName());
            addressVo.setPhone(address.getPhone());

            String district_ssn = RedisUse.get("district_ssn");
            List<District> districts = JSONArray.parseArray(district_ssn, District.class);

            String areaName = "";
            String areaIds = address.getAreaIds();

            for (int j = 0; j < districts.size(); j++) {
                if (areaIds.contains(","+districts.get(j).getId()+",")){
                    areaName += districts.get(j).getName();
                }
            }
            addressVo.setAddress(areaName+address.getDetailAdd());

            list.add(addressVo);
        }
        return list;
    }

    @Override
    public List getAddress() {
        //获取当前登录的用户的手机号
        Map login_user = (Map) request.getAttribute("login_user");
        String phone = (String) login_user.get("phone");

        Integer vipId = 0;

        if (phone.length() == 11){
            ShopUserVip vip = vipMapper.getVipByNumber(phone);
            vipId = vip.getId();
        }else {
            ShopUserVip vip = vipMapper.getVipByName(phone);
            vipId = vip.getId();
        }

        List<Address> address = addressMapper.selectByVipId(vipId);

        List list = new ArrayList();
        for (int i = 0; i < address.size(); i++) {
            Object address1 = address.get(i);
            String addressDeta =((Address) address1).getDetailAdd()+"  "+((Address) address1).getName()+"  "+((Address) address1).getPhone();
            list.add(addressDeta);
        }

        return list;


    }
}
