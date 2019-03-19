package com.soft863.pushmessge.util;

import cn.jiguang.common.utils.StringUtils;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.soft863.pushmessge.domain.AllMessage;
import com.soft863.pushmessge.domain.AndroidMessage;
import com.soft863.pushmessge.domain.IosMessage;

/**
 * @ClassName: PushPayLoadUtil
 * @Author
 * @Date 2019/3/18 0018
 */
public class PushPayLoadUtil {

    /**
     * 根据alias向Android用户推送消息
     * @param androidMessage 推送消息体
     * @param production
     * @param sendno
     * @param timeToLive
     * @return
     */
    public static PushPayload pushMessageToAndroidByAlias(AndroidMessage androidMessage, boolean production, int sendno,
                                                          long timeToLive) {
        if(androidMessage.getTagsOrAlias().isEmpty()){
            throw new PushMessageException("参数异常，alias不能为空", 11001);
        }
        if(StringUtils.isEmpty(androidMessage.getAlter())){
            throw new PushMessageException("参数异常，alter不能为空", 11002);
        }
        if(StringUtils.isEmpty(androidMessage.getTitle())){
            throw new PushMessageException("参数异常，title不能为空", 11003);
        }

        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(androidMessage.getTagsOrAlias()))
                .setNotification(Notification.android(androidMessage.getAlter(), androidMessage.getTitle(), androidMessage.getExtras()))
                .setMessage(Message.newBuilder().setMsgContent(androidMessage.getMsgContent()).build())
                .setOptions(OptionsUtil.options(production, sendno, timeToLive))
                .build();
    }

    /**
     * 根据tags型Android用户推送消息
     */
    public static PushPayload pushMessageToAndroidByTags(AndroidMessage androidMessage, boolean production, int sendno,
                                                         long timeToLive) {
        if(androidMessage.getTagsOrAlias().isEmpty()){
            throw new PushMessageException("参数异常，tags不能为空", 11001);
        }
        if(StringUtils.isEmpty(androidMessage.getAlter())){
            throw new PushMessageException("参数异常，alter不能为空", 11002);
        }
        if(StringUtils.isEmpty(androidMessage.getTitle())){
            throw new PushMessageException("参数异常，title不能为空", 11003);
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(androidMessage.getTagsOrAlias()))
                .setNotification(Notification.android(androidMessage.getAlter(), androidMessage.getTitle(), androidMessage.getExtras()))
                .setMessage(Message.newBuilder().setMsgContent(androidMessage.getMsgContent()).build())
                .setOptions(OptionsUtil.options(production, sendno, timeToLive))
                .build();
    }

    /**
     *  向Android用户推送消息
     */
    public static PushPayload pushMessageToAndroidAll(AndroidMessage androidMessage, boolean production, int sendno,
                                                      long timeToLive) {
        if(StringUtils.isEmpty(androidMessage.getAlter())){
            throw new PushMessageException("参数异常，alter不能为空", 11002);
        }
        if(StringUtils.isEmpty(androidMessage.getTitle())){
            throw new PushMessageException("参数异常，title不能为空", 11003);
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.android(androidMessage.getAlter(), androidMessage.getTitle(), androidMessage.getExtras()))
                .setMessage(Message.newBuilder().setMsgContent(androidMessage.getMsgContent()).build())
                .setOptions(OptionsUtil.options(production, sendno, timeToLive))
                .build();
    }

    /**
     *  根据tags向ios用户推送消息
     */
    public static PushPayload pushMessageToIosAllByTags(IosMessage messageDO, int badge, String sound, boolean production, int sendno, long timeToLive) {
        if(messageDO.getTagsOrAlias().isEmpty()){
            throw new PushMessageException("参数异常，tags不能为空", 11001);
        }
        if(StringUtils.isEmpty(messageDO.getAlter())){
            throw new PushMessageException("参数异常，alter不能为空", 11002);
        }
        if(StringUtils.isEmpty(messageDO.getMsgContent())){
            throw new PushMessageException("参数异常，msgContent不能为空", 11004);
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag(messageDO.getTagsOrAlias()))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(messageDO.getAlter())
                                .setBadge(badge)
                                .setSound(sound)
                                .addExtra(messageDO.getExtraKey(), messageDO.getExtraValue())
                                .build())
                        .build())
                .setMessage(Message.content(messageDO.getMsgContent()))
                .setOptions(OptionsUtil.options(production, sendno, timeToLive))
                .build();
    }

    /**
     *  向ios所有用户推送消息
     */
    public static PushPayload pushMessageToIosAll(IosMessage iosMessage, int badge, String sound, boolean production, int sendno, long timeToLive) {
        if(StringUtils.isEmpty(iosMessage.getAlter())){
            throw new PushMessageException("参数异常，alter不能为空", 11002);
        }
        if(StringUtils.isEmpty(iosMessage.getMsgContent())){
            throw new PushMessageException("参数异常，msgContent不能为空", 11004);
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(iosMessage.getAlter())
                                .setBadge(badge)
                                .setSound(sound)
                                .addExtra(iosMessage.getExtraKey(), iosMessage.getExtraValue())
                                .build())
                        .build())
                .setMessage(Message.content(iosMessage.getMsgContent()))
                .setOptions(OptionsUtil.options(production, sendno, timeToLive))
                .build();
    }

    /**
     *  向Android和ios平台所有用户推送消息
     */
    public static PushPayload pushMessageAndroidAndIosAll(AllMessage allMessage, boolean production, int sendno,
                                                          long timeToLive) {
        if(StringUtils.isEmpty(allMessage.getAlter())){
            throw new PushMessageException("参数异常，alter不能为空", 11002);
        }
        if(StringUtils.isEmpty(allMessage.getMsgContent())){
            throw new PushMessageException("参数异常，msgContent不能为空", 11004);
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .setAlert(allMessage.getAlter())
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent(allMessage.getMsgContent())
                        .addExtra(allMessage.getExtraKey(), allMessage.getExtraValue())
                        .build())
                .setOptions(OptionsUtil.options(production, sendno, timeToLive))
                .build();
    }
}
