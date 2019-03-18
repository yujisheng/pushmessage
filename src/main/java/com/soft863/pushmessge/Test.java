package com.soft863.pushmessge;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Test
 * @Author
 * @Date 2019/3/18 0018
 */
public class Test {
    public static void main(String[] args) throws APIConnectionException, APIRequestException {
        PushMessage pushMessage = new PushMessage();
//        pushMessage.setAppKey("ba47f4976d3cfb9e0482a58e");
//        pushMessage.setMasterSecret("2e1450220d6addb8808ad8ff");
//        pushMessage.setProduction(false);
//
//        pushMessage.pushMessageToAndroidAll("测试","测试标题","id","1");
//        List<String> alias = new ArrayList<>();
//        alias.add("alias1");
//        alias.add("alias2");
//        alias.add("alias3");
//        pushMessage.pushMessage(alias,"测试pushMessage","测试","extra","额外功能");

        List<String> tags = new ArrayList<>();
        tags.add("tags1");
        tags.add("tags2");
        tags.add("tags3");
        pushMessage.pushMessageToAllPlatformByTag(tags,"测试pushMessageToAllPlatformByTag","测试","extra","额外功能");
//        pushMessage.pushMessageToAndroidAll("android测试","测试","extra","额外功能");
    }
}
