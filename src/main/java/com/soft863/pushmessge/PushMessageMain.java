package com.soft863.pushmessge;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.Map;

public class PushMessageMain {
    private static String APP_KEY = "5d9a27ae41095e3b0b243984";
    private static String MASTER_SECRET = "6cb1c6c5e2779ac0257d8a72";
    private static boolean production = true;

    /**
     * 设置账号的app_key
     *
     * @param appKey
     */
    public static void setAppKey(String appKey) {
        APP_KEY = appKey;
    }

    /**
     * 设置账号的masterSecret
     *
     * @param masterSecret
     */
    public static void setMasterSecret(String masterSecret) {
        MASTER_SECRET = masterSecret;
    }

    /**
     * 指定开发环境，true为生产环境，false为测试环境
     *
     * @param production
     */
    public static void setProduction(boolean production) {
        PushMessageMain.production = production;
    }

    /**
     * 极光消息推送
     *
     * @param parm map集合，“id”制定用户id，"msg“指定消息体
     */
    public static void jpushAll(Map<String, String> parm) {
        //创建JPushClient
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        //创建option
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())  //所有平台的用户
                .setAudience(Audience.registrationId(parm.get("id")))//registrationId指定用户
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder() //发送ios
                                .setAlert(parm.get("msg")) //消息体
                                .setBadge(+1)
                                .setSound("happy") //ios提示音
                                .addExtras(parm) //附加参数
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder() //发送android
                                .addExtras(parm) //附加参数
                                .setAlert(parm.get("msg")) //消息体
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(production).build())//指定开发环境 true为生产模式 false 为测试模式
                // (android不区分模式,ios区分模式)
                .setMessage(Message.newBuilder().setMsgContent(parm.get("msg")).addExtras(parm).build())//自定义信息
                .build();
        try {
            PushResult pu = jpushClient.sendPush(payload);
            System.out.println(pu.toString());
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }

    }


}
