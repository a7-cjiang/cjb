package com.fh.biz;

import java.util.Map;

public interface VipService {
    String getPhone(String phone);

    Map login(String phone,String code);
}
