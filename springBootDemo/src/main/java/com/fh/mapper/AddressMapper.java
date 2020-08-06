package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.po.Address;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressMapper extends BaseMapper<Address> {

    @Select("select * from shop_address where vipId = #{vipId}")
    List<Address> selectByVipId(Integer vipId);
}
