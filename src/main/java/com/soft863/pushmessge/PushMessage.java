package com.soft863.pushmessge;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.soft863.pushmessge.config.*;
import com.soft863.pushmessge.util.PlatfromNotificationUtil;
import com.soft863.pushmessge.util.PushPayloadUtil;
import com.soft863.pushmessge.util.PushMessageException;
import com.sun.javafx.PlatformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PushMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessage.class);
    private static String appKey = "";
    private static String masterSecret = "";
    private static boolean production = true;
    private static long timeToLive = 86400;
    private static int sendno = 1;
    private static int badge = 1;
    private static String sound = "default";


    private static JPushClient jPushClient = null;



    /**
     * 推送给设备标识参数的用户
     *
     * @param aliasList   别名或别名组
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int pushMessage(JPushClient jPushClient, List<String> aliasList, String msgContent, String notificationTitle, String extraKey, String extrasparam) {
        PushPayload pushPayload = pushMessageToAllPlatfromByAliasList(aliasList, notificationTitle, msgContent, extraKey, extrasparam);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload);
    }

    /**
     * 发送给所有用户
     *
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAll(JPushClient jPushClient, String msgContent, String notificationTitle, String extraKey, String extrasparam) {
        PushPayload pushPayload = pushMessageToAllPlatfromAll(msgContent, notificationTitle, extraKey, extrasparam);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload);
    }

    /**
     * 推送给Tag参数的用户
     *
     * @param tagsList    Tag或Tag组
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToTagList(JPushClient jPushClient, List<String> tagsList, String msgContent, String extraKey
            , String extrasparam, String notificationTitle) {
        PushPayload pushPayload = pushMessageToAllPlatfromByTagList(tagsList, notificationTitle, msgContent, extraKey, extraKey);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload);
    }

    /**
     * 发送给所有安卓用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgContent        消息内容
     * @param extrasparam        扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllAndroid(JPushClient jPushClient, String msgContent, String notificationTitle, String extraKey, String extrasparam) {
        PushPayload pushPayload = pushMessageToAndroidPlatfromAll(msgContent, notificationTitle, extraKey, extraKey);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload);
    }

    /**
     * 发送给所有IOS用户
     *
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return
     */
    public static int pushMessageToAllIos(JPushClient jPushClient, String msgContent, String extraKey, String extrasparam) {
        PushPayload pushPayload = pushMessageToIosPlatfromAll(msgContent, extraKey, extraKey);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload);
    }

    /**
     * 向android平台所有用户推送消息
     *
     * @param msgContent        消息内容体，指要推送的消息
     * @param notificationTitle 要推送的通知的主题
     * @param extraKey           扩展字段
     * @param extraValue         扩展内容
     * @return
     */
    private static PushPayload pushMessageToAndroidPlatfromAll(String msgContent, String notificationTitle, String extraKey, String extraValue) {
        LOGGER.info("==================向android平台所有用户推送消息中==================");
        // 获取推送对象
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        // 设置消息推送平台
        pushPayloadBuilder.setPlatform(Platform.android());
        // 指定推送消息的接受对象，all()代表所有对象
        pushPayloadBuilder.setAudience(Audience.all());
        // 设置通知内容体
        pushPayloadBuilder.setNotification(PlatfromNotificationUtil.androidNotification(msgContent, notificationTitle, extraKey, extraValue));
        // 设置推送参数
        pushPayloadBuilder.setOptions(PushPayloadUtil.options(production, sendno, timeToLive));

        return pushPayloadBuilder.build();
    }

    /**
     * 向ios平台所有用户推送消息
     *
     * @param msgContent 要推送的消息
     * @param extraKey    扩展字段
     * @param extraValue  扩展字段内容
     * @return
     */
    private static PushPayload pushMessageToIosPlatfromAll(String msgContent, String extraKey, String extraValue) {
        LOGGER.info("----------向ios平台所有用户推送消息中.......");
        // 获取推送对象
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        // 设置消息推送平台
        pushPayloadBuilder.setPlatform(Platform.ios());
        // 指定推送消息的接受对象，all()代表所有对象
        pushPayloadBuilder.setAudience(Audience.all());
        // 设置通知内容体
        pushPayloadBuilder.setNotification(PlatfromNotificationUtil.iosNotification(msgContent, badge, sound, extraKey, extraValue));
        // 设置推送参数
        pushPayloadBuilder.setOptions(PushPayloadUtil.options(production, sendno, timeToLive));

        return pushPayloadBuilder.build();
    }

    /**
     * 向所有平台，单个或多个指定Tag用户推送消息
     *
     * @param tagsList           用户的tag的集合
     * @param msgContent
     * @param notificationTitle
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PushPayload pushMessageToAllPlatfromByTagList(List<String> tagsList, String notificationTitle, String msgContent,
                                                                 String extraKey, String extraValue) {
        LOGGER.info("----------向所有平台单个或多个指定Tag用户推送消息中.......");

        // 获取推送对象
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        // 设置消息推送平台
        pushPayloadBuilder.setPlatform(Platform.all());
        //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
        pushPayloadBuilder.setAudience(Audience.tag(tagsList));
        // 设置通知内容体
        pushPayloadBuilder.setNotification(PlatfromNotificationUtil.allNotification(msgContent, notificationTitle, badge, sound, extraKey, extraValue));
        // 设置推送参数
        pushPayloadBuilder.setOptions(PushPayloadUtil.options(production, sendno, timeToLive));

        return pushPayloadBuilder.build();
    }

    /**
     * 向所有平台所有用户推送消息
     *
     * @param msgContent
     * @param notificationTitle
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PushPayload pushMessageToAllPlatfromAll(String msgContent, String notificationTitle, String extraKey, String extraValue) {


        LOGGER.info("----------向所有平台所有用户推送消息中......");

        // 获取推送对象
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        // 设置消息推送平台
        pushPayloadBuilder.setPlatform(Platform.all());
        //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
        pushPayloadBuilder.setAudience(Audience.all());
        // 设置通知内容体
        pushPayloadBuilder.setNotification(PlatfromNotificationUtil.allNotification(msgContent, notificationTitle, badge, sound, extraKey, extraValue));
        // 设置推送参数
        pushPayloadBuilder.setOptions(PushPayloadUtil.options(production, sendno, timeToLive));

        return pushPayloadBuilder.build();

    }

    /**
     * 向所有平台单个或多个指定别名用户推送消息
     *
     * @param aliasList
     * @param msgContent
     * @param notificationTitle
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PushPayload pushMessageToAllPlatfromByAliasList(List<String> aliasList, String notificationTitle, String msgContent,
                                                                   String extraKey, String extraValue) {

        LOGGER.info("----------向所有平台单个或多个指定别名用户推送消息中......");

        // 获取推送对象
        PushPayload.Builder pushPayloadBuilder = PushPayload.newBuilder();
        // 设置消息推送平台
        pushPayloadBuilder.setPlatform(Platform.all());
        //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
        pushPayloadBuilder.setAudience(Audience.alias(aliasList));
        // 设置通知内容体
        pushPayloadBuilder.setNotification(PlatfromNotificationUtil.allNotification(msgContent, notificationTitle, badge, sound, extraKey, extraValue));
        // 设置推送参数
        pushPayloadBuilder.setOptions(PushPayloadUtil.options(production, sendno, timeToLive));

        return pushPayloadBuilder.build();
    }


    /**
     * 设置appKey
     *
     * @param appKey
     */
    public static void setAppKey(String appKey) {
        PushMessage.appKey = appKey;
    }

    /**
     * 设置极光私钥masterSeect
     *
     * @param masterSecret
     */
    public static void setMasterSecret(String masterSecret) {
        PushMessage.masterSecret = masterSecret;
    }

    /**
     * 设置开发环境，true为生产环境，false为测试环境
     *
     * @param production
     */
    public static void setProduction(boolean production) {
        PushMessage.production = production;
    }

    /**
     * 设置推送的离线保存时长timeToLive
     *
     * @param timeToLive
     */
    public static void setTimeToLive(long timeToLive) {
        PushMessage.timeToLive = timeToLive;
    }

}
