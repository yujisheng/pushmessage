package com.soft863.pushmessge.Util1;

import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: PushPayLoadUtil
 * @Author
 * @Date 2019/3/18 0018
 */
public class PushPayLoadUtil {

    public static PushPayload pushMessageToAndroidByTag(List<String> tags, String alter, String title, Map<String,
            String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setNotification(Notification.android(alter, title, extras))
                .setAudience(Audience.tag(tags))
                //.setOptions(OptionsUtil.options(producttion, sendno, timeToLive))
                .build();
    }
    public static PushPayload pushMessageToAndroidByAlias(List<String> alias, String alter, String title, Map<String,
            String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.android(alter, title, extras))
                //.setOptions(OptionsUtil.options(producttion, sendno, timeToLive))
                .build();
    }
    public static PushPayload pushMessageToAndroidAll(String alter, String title, Map<String,
            String> extras) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.android(alter, title, extras))
                //.setOptions(OptionsUtil.options(producttion, sendno, timeToLive))
                .build();
    }

    /*private PushPayload.Builder androidPushPayloadBuilder(String ALERT, String TITLE, Map<String,String> extras){
        PushPayload.Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.android(ALERT, TITLE, extras));
        return builder;
    }*/




    /*public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and("tag1", "tag_all"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(ALERT)
                                .setBadge(5)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(MSG_CONTENT))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(MSG_CONTENT)
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }*/
}
