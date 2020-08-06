package com.fh.utils;

public class SnowUtil {

    public static Long snow(){
        Snow snow = new Snow(1,1,1);
        long snowNum = 0;
        for (int i = 0; i < 1; i++) {
            System.out.println(snow.nextId());
            snowNum = snow.nextId();
        }
        return snowNum;
    }

}
