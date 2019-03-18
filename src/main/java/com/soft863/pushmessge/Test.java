package com.soft863.pushmessge;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.soft863.pushmessge.domain.IosMessage;
import com.soft863.pushmessge.domain.MessageDO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: Test
 * @Author
 * @Date 2019/3/18 0018
 */
public class Test {
    public static void main(String[] args) throws APIConnectionException, APIRequestException {
        List<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        List<String> alias = new ArrayList<String>();
        alias.add("alias1");
        alias.add("alias2");
        alias.add("alias3");
        Map<String,String> extras = new HashMap<String, String>();
        extras.put("extra","额外功能");

        MessageDO messageDO = new MessageDO(tags, "测试pushMessageAndroidByTags", "测试", extras);
        MessageDO messageDO1 = new MessageDO(alias, "测试pushMessageAndroidByAlias", "测试", extras);
        MessageDO messageDO2 = new MessageDO("测试pushMessageAndroidAll", "测试", extras);

        PushMessage pushMessage = new PushMessage();
        pushMessage.setAppKey("ba47f4976d3cfb9e0482a58e");
        pushMessage.setMasterSecret("2e1450220d6addb8808ad8ff");
//        pushMessage.pushMessageToAndroidByAlias(messageDO1);
//        pushMessage.pushMessageToAndroidByTags(messageDO);
//        pushMessage.pushMessageToAndroidAll(messageDO2);

        IosMessage iosMessage = new IosMessage("测试pushMessageToIOsAll", "消息内容", "extraKey", "extraValue");
        IosMessage iosMessage1 = new IosMessage(tags,"测试pushMessageToIOsByTags", "消息内容", "extraKey", "extraValue");
//        pushMessage.pushMessageToIosAll(iosMessage);
        pushMessage.pushMessageToIosByTags(iosMessage1);
    }

}
