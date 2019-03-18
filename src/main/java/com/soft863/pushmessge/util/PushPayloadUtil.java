package com.soft863.pushmessge.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.utils.StringUtils;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.PushPayload;
import com.soft863.pushmessge.config.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.resources.cldr.ff.LocaleNames_ff;

import java.util.List;

/**
 * 推送工具
 *
 * @ClassName PushPayloadUtil
 * @Author
 * @Date 2019/3/18 0018
 */
public class PushPayloadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushPayloadUtil.class);

    /**
     * 推送参数
     *
     * @param production 指定本推送要推送的apns环境
     * @param sendno     推送序号
     * @param timeToLive 指定本推送的离线保存时长
     * @return
     */
    public static Options options(boolean production, int sendno, long timeToLive) {
        // 设置推送参数
        Options.Builder optionsBuilder = Options.newBuilder();
        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
        optionsBuilder.setApnsProduction(production);
        //  推送序号纯粹用来作为 API 调用标识，API 返回时被原样返回，以方便 API 调用方匹配请求与返回。
        optionsBuilder.setSendno(sendno);
        // 指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
        optionsBuilder.setTimeToLive(timeToLive);
        return optionsBuilder.build();
    }

    /**
     * 推送结果
     * @param jPushClient
     * @param pushPayload
     * @param aliasOrTags 推送标识
     * @param notificationTitle 推送通知的主题
     * @param msgContent 推送消息内容
     * @return
     */
    public static int pushResult(JPushClient jPushClient, PushPayload pushPayload , List<String> aliasOrTags,
                                 String notificationTitle, String msgContent) {
        if(!aliasOrTags.isEmpty() && StringUtils.isNotEmpty(msgContent) && StringUtils.isNotEmpty(notificationTitle)) {
            // 消息推送
            sendPushMessage(jPushClient,pushPayload);
        }else {
            ThrowException.aliasOrTagsException(aliasOrTags);
            ThrowException.notificationTitleException(notificationTitle);
            ThrowException.messageContentException(msgContent);
        }
        return com.soft863.pushmessge.config.PushResult.PUSH_FAILURE;
    }


    public static int pushResult(JPushClient jPushClient, PushPayload pushPayload,
                                 String notificationTitle, String msgContent) {
        if(StringUtils.isNotEmpty(msgContent) && StringUtils.isNotEmpty(notificationTitle)) {
            sendPushMessage(jPushClient,pushPayload);
        }else {
            ThrowException.notificationTitleException(notificationTitle);
            ThrowException.messageContentException(msgContent);
        }
        return com.soft863.pushmessge.config.PushResult.PUSH_FAILURE;
    }

    public static int pushResult(JPushClient jPushClient, PushPayload pushPayload, String msgContent) {
        if(StringUtils.isNotEmpty(msgContent)) {
            sendPushMessage(jPushClient,pushPayload);
        }else {
            ThrowException.messageContentException(msgContent);
        }
        return com.soft863.pushmessge.config.PushResult.PUSH_FAILURE;
    }

    /**
     * 推送消息
     * @param jPushClient
     * @param pushPayload
     * @return
     */
    private static int sendPushMessage(JPushClient jPushClient, PushPayload pushPayload){
        LOGGER.info("===========sendPush==================");
        try {
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                return com.soft863.pushmessge.config.PushResult.PUSH_SUCCESS;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return com.soft863.pushmessge.config.PushResult.PUSH_FAILURE;
    }



}
