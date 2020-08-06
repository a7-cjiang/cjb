package com.fh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.po.Merchandise;
import com.fh.po.vo.CartVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MerchandiseMapper extends BaseMapper<Merchandise> {

    @Select("select id,price,img,name from shop_merchandise where isSale = 1 ")
    List<Merchandise> getHotProduct();


    List<Merchandise> getProdecuBytypeId(@Param("typeId") Integer typeId);

    @Select("select * from shop_merchandise where id = #{id}")
    Merchandise getProductById(Integer id);

    @Select("select id,name,price,img from shop_merchandise where id =#{id}")
    CartVo getProToCartById(Integer id);

    // 传递多个值   1  javabean  2 map  3 @param
    Integer updateProductCount(@Param("id") Integer id,@Param("count") Integer count);
}
