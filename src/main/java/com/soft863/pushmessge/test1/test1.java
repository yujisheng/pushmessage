package com.soft863.pushmessge.test1;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: test1
 * @Author
 * @Date 2019/3/18 0018
 */
public class test1 {
    public static void main(String[] args) {
        HashMap<String, String> param = new HashMap<>();
        param.put("msg","hello ,测试");
        Jdpush.jpushAndroid(param);
    }

}
