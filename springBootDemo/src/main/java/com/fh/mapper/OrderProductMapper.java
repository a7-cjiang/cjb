package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.po.OrderProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderProductMapper extends BaseMapper<OrderProduct> {

    void batchAdd(@Param("list") List<OrderProduct> list, @Param("oid") Integer oid);

    @Select("select * from shop_order_product where orderId = #{id}")
    OrderProduct selectByOrderId(Integer id);

    @Select("select sop.*,sm.name from shop_order_product sop left join shop_merchandise sm on sop.productId = sm.id where sop.orderId = #{id}")
    List<OrderProduct> getOrderDeta(Integer id);
}
