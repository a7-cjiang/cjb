package com.fh.biz;

import com.fh.po.Merchandise;

import java.util.List;

public interface MerchandiseService {
    String getHotMerchandise();

    List<Merchandise> getProdecuBytypeId(Integer typeId);

    Merchandise getProductById(Integer id);
}
