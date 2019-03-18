package com.soft863.pushmessge.util;

import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.soft863.pushmessge.config.PushResult;

/**
 * 推送平台通知消息体
 *
 * @ClassName PlatfromNotificationUtil
 *
 * @Author
 * @Date 2019/3/18 0018
 */
public class PlatfromNotificationUtil {

    /**
     * 获取Android平台的通知内容体
     *
     * @param msgContent
     * @param notificationTitle
     * @param extraKey
     * @param extraValue
     * @return
     */
    public static Notification androidNotification(String msgContent, String notificationTitle, String extraKey, String extraValue) {
        // 获取android平台的通知内容体
        PlatformNotification platformNotification = androidNotificationBuilder(msgContent, notificationTitle, extraKey, extraValue);
        // 获取通知内容体
        Notification.Builder notificationBuilder = Notification.newBuilder().addPlatformNotification(platformNotification);
        return notificationBuilder.build();
    }

    private static PlatformNotification androidNotificationBuilder(String msgContent, String notificationTitle, String extraKey, String extraValue) {
        // 获取Android内容体对象
        AndroidNotification.Builder androidNotificationBuilder = AndroidNotification.newBuilder();
        // 设置推送的消息
        androidNotificationBuilder.setAlert(msgContent);
        // 设置推送消息的主题
        androidNotificationBuilder.setTitle(notificationTitle);
        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
        androidNotificationBuilder.addExtra(extraKey, extraValue);
        // 获取对应平台的通知内容体
        return androidNotificationBuilder.build();

    }

    /**
     * 获取ios平台的通知内容体（通知内容体）
     *
     * @param iosAlert
     * @param badge
     * @param sound
     * @param extraKey
     * @param extraValue
     * @return
     */
    public static Notification iosNotification(Object iosAlert, int badge, String sound, String extraKey, String extraValue) {
        // 获取iOS平台的通知内容体
        PlatformNotification platformNotification = iosNotificationBuilder(iosAlert, badge, sound, extraKey, extraValue);
        // 获取内容体
        Notification.Builder notificationBuilder = Notification.newBuilder().addPlatformNotification(platformNotification);
        return notificationBuilder.build();
    }

    /**
     * 获取iOS平台的通知内容体
     *
     * @param iosAlert
     * @param badge
     * @param sound
     * @param extraKey
     * @param extraValue
     * @return
     */
    private static PlatformNotification iosNotificationBuilder(Object iosAlert, int badge, String sound, String extraKey, String extraValue) {
        // 获取ios内容体对象
        IosNotification.Builder iosNotificationBuilder = IosNotification.newBuilder();
        // 设置推送的消息(这里传一个IosAlert对象，指定apns title、title、subtitle等)
        iosNotificationBuilder.setAlert(iosAlert);
        // 应用角标
        // 如果不填，表示不改变角标数字，否则把角标数字改为指定的数字；为 0 表示清除。
        // JPush 官方 SDK 会默认填充 badge 值为 "+1"
        iosNotificationBuilder.incrBadge(badge);
        // 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
        // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
        iosNotificationBuilder.setSound(sound);
        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
        iosNotificationBuilder.addExtra(extraKey, extraValue);
        // 获取对应平台的通知内容体
        return iosNotificationBuilder.build();
    }

    /**
     * 获取所有平台通知内容体
     *
     * @param msgContent
     * @param notificationTitle
     * @param badge
     * @param sound
     * @param extraKey
     * @param extraValue
     * @return
     */
    public static Notification allNotification(String msgContent, String notificationTitle, int badge, String sound, String extraKey, String extraValue) {
        // 获取通知内容体
        Notification.Builder notificationBuilder = Notification.newBuilder();
        notificationBuilder.addPlatformNotification(iosNotificationBuilder(msgContent, badge, sound, extraKey, extraValue));
        notificationBuilder.addPlatformNotification(androidNotificationBuilder(msgContent, notificationTitle, extraKey, extraValue));
        return notificationBuilder.build();
    }

}
