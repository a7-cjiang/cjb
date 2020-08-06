package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.po.ShopUserVip;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface VipMapper extends BaseMapper<ShopUserVip> {

    @Select("select number,id from shop_user_vip where name = #{phone}")
    String getPhone(String phone);

    @Select("select * from shop_user_vip where number = #{phone}")
    ShopUserVip getVipByNumber(String phone);

    @Select("select * from shop_user_vip where name = #{phone}")
    ShopUserVip getVipByName(String phone);
}
