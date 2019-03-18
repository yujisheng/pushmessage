package com.soft863.pushmessge;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.utils.StringUtils;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.soft863.pushmessge.util.PlatfromNotificationUtil;
import com.soft863.pushmessge.util.PushPayloadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *  消息推送
 *
 * @ClassName PushMessage
 * @Author
 * @Date 2019/3/18 0018
 */
public class PushMessage {
    private final Logger LOGGER = LoggerFactory.getLogger(PushMessage.class);

    private String appKey = "";
    private String masterSecret = "";
    private boolean production = true;
    private long timeToLive = 86400;
    private int sendno = 1;
    private int badge = 1;
    private String sound = "default";




    /**
     * 推送给设备标识参数的用户
     *
     * @param aliasList   别名或别名组
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return
     */
    public int pushMessage(List<String> aliasList, String msgContent, String notificationTitle, String extraKey,
                            String extrasparam) {
        JPushClient jPushClient = new JPushClient(masterSecret,appKey);
        PushPayload pushPayload = pushMessageToAllPlatfromByAliasList(aliasList, notificationTitle, msgContent, extraKey, extrasparam);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload, aliasList, notificationTitle, msgContent);
    }

    /**
     * 发送给所有用户
     *
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return
     */
    public int pushMessageToAllPlatfrom(String msgContent, String notificationTitle, String extraKey,
                                        String extrasparam) {
        JPushClient jPushClient = new JPushClient(masterSecret,appKey);
        PushPayload pushPayload = pushMessageToAllPlatfromAll(msgContent, notificationTitle, extraKey, extrasparam);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload,notificationTitle,msgContent);

    }

    /**
     * 推送给Tag参数的用户
     *
     * @param tagsList    Tag或Tag组
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return
     */
    public int pushMessageToAllPlatformByTag(List<String> tagsList, String msgContent, String extraKey
            , String extrasparam, String notificationTitle) {
        JPushClient jPushClient = new JPushClient(masterSecret,appKey);
        PushPayload pushPayload = pushMessageToAllPlatfromByTagList(tagsList, notificationTitle, msgContent, extraKey, extrasparam);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload, tagsList,notificationTitle,msgContent);
    }

    /**
     * 发送给所有android用户
     *
     * @param notificationTitle 通知内容标题
     * @param msgContent        消息内容
     * @param extrasparam        扩展字段
     * @return
     */
    public int pushMessageToAndroidAll(String msgContent, String notificationTitle, String extraKey,
                                      String extrasparam) {
        JPushClient jPushClient = new JPushClient(masterSecret,appKey);
        PushPayload pushPayload = PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msgContent)
                                .setTitle(notificationTitle)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey,extrasparam)
                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtra("message extras key",extrasparam)
                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(false)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
        PushResult pushResult= null;
        try {
            pushResult = jPushClient.sendPush(pushPayload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        LOGGER.info(""+pushResult);
//        if(pushResult.getResponseCode()==200){
//            result=1;
//        }
//
        return 0;
//        PushPayload pushPayload = pushMessageToAndroidPlatfromAll(msgContent, notificationTitle, extraKey, extrasparam);
//        return PushPayloadUtil.pushResult(jPushClient, pushPayload,notificationTitle,msgContent);
    }

    /**
     * 发送给所有IOS用户
     *
     * @param msgContent 消息内容
     * @param extrasparam 扩展字段
     * @return
     */
    public int pushMessageToIosAll(String msgContent, String extraKey, String extrasparam) {
        JPushClient jPushClient = new JPushClient(this.appKey,this.masterSecret);
        PushPayload pushPayload = pushMessageToIosPlatfromAll(msgContent, extraKey, extrasparam);
        return PushPayloadUtil.pushResult(jPushClient, pushPayload, msgContent);
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
    private PushPayload pushMessageToAndroidPlatfromAll(String msgContent, String notificationTitle, String extraKey
            , String extraValue) {
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
    private PushPayload pushMessageToIosPlatfromAll(String msgContent, String extraKey, String extraValue) {
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
    private PushPayload pushMessageToAllPlatfromByTagList(List<String> tagsList, String notificationTitle,
                                                           String msgContent,
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
    private PushPayload pushMessageToAllPlatfromAll(String msgContent, String notificationTitle, String extraKey,
                                                     String extraValue) {


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
    private PushPayload pushMessageToAllPlatfromByAliasList(List<String> aliasList, String notificationTitle,
                                                                    String msgContent,
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
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * 设置极光私钥masterSeect
     *
     * @param masterSecret
     */
    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    /**
     * 设置开发环境，true为生产环境，false为测试环境
     *
     * @param production
     */
    public void setProduction(boolean production) {
        this.production = production;
    }

    /**
     * 设置推送的离线保存时长timeToLive
     *
     * @param timeToLive
     */
    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

}
