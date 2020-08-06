package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.po.Order;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderMapper extends BaseMapper<Order> {

    @Select("select * from shop_order so left join shop_user_vip suv on so.vipId = suv.id where vipId = #{phone}")
    List<Order> getOrderByPhone(String phone);

}
