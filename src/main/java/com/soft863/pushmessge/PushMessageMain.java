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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class PushMessageMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushMessageMain.class);

    private static String APP_KEY = "";
    private static String MASTER_SECRET = "";
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
     * 针对所有平台的用户
     * @param parm map集合，“id”制定用户id，"msg“指定消息体
     */
    public static void jpushAll(Map<String, String> parm) throws Exception {
        if (MASTER_SECRET.length() > 0 && APP_KEY.length() > 0) {
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
        } else {
            throw new Exception("appkey或masterSecret不能为空！");
        }
    }

    /**
     * 推送给设备标识参数的用户
     *
     * @param aliasList   别名或别名组
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAliasList(JPushClient jPushClient, List<String> aliasList, String msg_content, String extraKey, String extrasparam, String notification_title) {
        int result = 0;
        try {
            PushPayload pushPayload = buildPushObject_all_aliasList_alertWithTitle(aliasList, msg_content, extraKey, extrasparam, notification_title);
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
    public static int sendToAll(JPushClient jPushClient, String msg_content, String extraKey, String extrasparam, String notification_title) {
        int result = 0;
        try {
            PushPayload pushPayload = buildPushObject_android_and_ios(msg_content, extraKey, extrasparam, notification_title);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == 200) {
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
    public static int sendToTagList(JPushClient jPushClient, List<String> tagsList, String msg_content, String extra, String extrasparam, String notification_title) {
        int result = 0;
        try {
            PushPayload pushPayload = buildPushObject_all_tagList_alertWithTitle(tagsList, msg_content, extra, extrasparam, notification_title);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
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
            PushPayload pushPayload = buildPushObject_android_all_alertWithTitle(notification_title, msg_content, extraKey, extrasparam);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == 200) {
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
    public static int sendToAllIos(JPushClient jPushClient, String msg_content, String extraKey, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload = buildPushObject_ios_all_alertWithTitle(msg_content, extraKey, extrasparam);
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }


    /**
     * 向所有平台单个或多个指定别名用户推送消息
     *
     * @param aliasList
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_all_aliasList_alertWithTitle(List<String> aliasList, String msg_content, String extraKey, String extrasparam, String notification_title) {

        LOGGER.info("----------向所有平台单个或多个指定别名用户推送消息中......");
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();

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
                                .addExtra(extraKey, extrasparam)

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
                                .addExtra(extraKey, extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)

                                .build())


                        .build())
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
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)

                        .build())

                .build();

    }


    /**
     * 向所有平台所有用户推送消息
     *
     * @param msg_content
     * @param extrasparam
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios(String msg_content, String extraKey, String extrasparam, String notification_title) {
        LOGGER.info("----------向所有平台所有用户推送消息中......");
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                                .setAlert(msg_content)
                                .addPlatformNotification(AndroidNotification.newBuilder()
                                        .setTitle(notification_title)
                                        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                        .addExtra(extraKey, extrasparam)
                                        .build()
                                )
                                .addPlatformNotification(IosNotification.newBuilder()
//                                //传一个IosAlert对象，指定apns title、title、subtitle等
//                                .setAlert(notification_title)
                                                //直接传alert
                                                //此项是指定此推送的badge自动加1
                                                .incrBadge(1)
                                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                                .setSound("default")
                                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                                .addExtra(extraKey, extrasparam)
                                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                                // .setContentAvailable(true)

                                                .build()
                                )
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
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build()
                )
                .build();
    }

    /**
     * 向所有平台单个或多个指定Tag用户推送消息
     *
     * @param tagsList
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_all_tagList_alertWithTitle(List<String> tagsList, String msg_content, String extraKey, String extrasparam, String notification_title) {

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
                                .addExtra(extraKey, extrasparam)

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
                                .addExtra(extraKey, extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)

                                .build())


                        .build())
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
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)

                        .build())

                .build();

    }

    /**
     * 向android平台所有用户推送消息
     *
     * @param notification_title
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_android_all_alertWithTitle(String notification_title, String msg_content, String extraKey, String extrasparam) {
        LOGGER.info("----------向android平台所有用户推送消息中......");
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extrasparam)
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
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }

    /**
     * 向ios平台所有用户推送消息
     *
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_ios_all_alertWithTitle(String msg_content, String extraKey, String extrasparam) {
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
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("default")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra(extraKey, extrasparam)
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
                        .addExtra("message extras key",extrasparam)
                        .build())*/

                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(production)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(86400)
                        .build())
                .build();
    }


}
