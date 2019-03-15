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
import com.soft863.pushmessge.config.HttpStatus;
import com.sun.jmx.remote.security.NotificationAccessController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PushMessage {
    private static String appKey = "";
    private static String masterSecret = "";
    private static boolean production = true;
    private static long timeToLive = 86400;

    private static JPushClient jPushClient = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessage.class);
    private int flag = 0; // 返回值标记

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

    /**
     * 推送给设备标识参数的用户
     *
     * @param aliasList   别名或别名组
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int pushMessage(JPushClient jPushClient, List<String> aliasList, String notification_title,
                                  String msg_content, String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload = pushMessageToAllPlatfromByAliasList(aliasList, notification_title, msg_content, extraKey, extrasparam);
            LOGGER.info("推送给设备标识参数的用户" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("推送结果" + pushResult);
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();

        } catch (APIRequestException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送给所有用户
     *
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAll(JPushClient jPushClient, String notification_title, String msg_content, String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload = pushMessageToAndroidAndIos(notification_title, msg_content, extraKey,
                    extrasparam);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                result = 1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * 推送给Tag参数的用户
     *
     * @param tagsList    Tag或Tag组
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToTagList(JPushClient jPushClient, List<String> tagsList, String msg_content, String extraKey
            , String extrasparam, String notification_title) {
        int result = 0;
        try {
            PushPayload pushPayload = pushMessageToAllPlatfromByTagList(tagsList, notification_title, msg_content,
                    extraKey, extrasparam);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                result = 1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();

        } catch (APIRequestException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送给所有安卓用户
     *
     * @param notification_title 通知内容标题
     * @param msg_content        消息内容
     * @param extrasparam        扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllAndroid(JPushClient jPushClient, String notification_title, String msg_content, String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload = pushMessageToAndroidPlatfromAll(notification_title, msg_content, extraKey, extrasparam);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                result = 1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送给所有IOS用户
     *
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int pushMessageToAllIos(JPushClient jPushClient, String msg_content, String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload = pushMessageToIosPlatfromAll(msg_content, extraKey, extrasparam);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                result = 1;
            }
        } catch (Exception e) {
            throw new PushMessageException("基础框架异常", 11001, e);
        }
        return result;
    }

    /**
     * 向android平台所有用户推送消息
     *
     * @param msg_content        消息内容体，指要推送的消息
     * @param notification_title 要推送的通知的主题
     * @param extraKey           扩展字段
     * @param extraValue         扩展内容
     * @return
     */
    private static PushPayload pushMessageToAndroidPlatfromAll(String notification_title, String msg_content,
                                                               String extraKey, String extraValue) {
        LOGGER.info("==================向android平台所有用户推送消息中==================");
        jPushClient = new JPushClient(appKey, masterSecret);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android()) // 指定推送平台;
                .setAudience(Audience.all()) // 指定推送消息的接受对象，all()代表所有对象
                .setNotification(Notification.newBuilder()
                        // 指定当前向Android推送的通知内容体
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                // 消息内容体，指被推送到客户端的内容
                                .setAlert(msg_content)
                                // 指定通知内容标题
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                .build()
                        ).build()
                )
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(timeToLive)
                        .build())
                .build();
    }

    /**
     * 向ios平台所有用户推送消息
     *
     * @param msg_content 要推送的消息
     * @param extraKey    扩展字段
     * @param extraValue  扩展字段内容
     * @return
     */
    private static PushPayload pushMessageToIosPlatfromAll(String msg_content, String extraKey, String extraValue) {
        LOGGER.info("----------向ios平台所有用户推送消息中.......");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.ios())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //应用角标,Push 官方 SDK 会默认填充 badge 值为 "+1"
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                //如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)

                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                /*.setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtra("message extras key",extraValue)
                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(timeToLive)
                        .build())
                .build();
    }

    /**
     * 向所有平台，单个或多个指定Tag用户推送消息
     *
     * @param tagsList           用户的tag的集合
     * @param msg_content
     * @param notification_title
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PushPayload pushMessageToAllPlatfromByTagList(List<String> tagsList, String notification_title, String msg_content,
                                                                 String extraKey, String extraValue) {
        LOGGER.info("----------向所有平台单个或多个指定Tag用户推送消息中.......");
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.tag(tagsList))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build()
                        ).build()
                )
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(timeToLive)
                        .build())
                .build();

    }

    /**
     * 向所有平台所有用户推送消息
     *
     * @param msg_content
     * @param notification_title
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PushPayload pushMessageToAndroidAndIos(String notification_title, String msg_content, String extraKey, String extraValue) {


        LOGGER.info("----------向所有平台所有用户推送消息中......");
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(msg_content)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                .build()
                        )
                        .addPlatformNotification(IosNotification.newBuilder()
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                // .setContentAvailable(true)
                                .build()
                        ).build()
                )

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(timeToLive)
                        .build()
                ).build();
    }

    /**
     * 向所有平台单个或多个指定别名用户推送消息
     *
     * @param aliasList
     * @param msg_content
     * @param notification_title
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PushPayload pushMessageToAllPlatfromByAliasList(List<String> aliasList, String notification_title, String msg_content,
                                                                   String extraKey, String extraValue) {

        LOGGER.info("----------向所有平台单个或多个指定别名用户推送消息中......");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.alias(aliasList))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extraValue)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build()
                        ).build()
                )
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(timeToLive)
                        .build()
                ).build();

    }
}
