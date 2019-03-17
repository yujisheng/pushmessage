package com.soft863.pushmessge.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.PushPayload;
import com.soft863.pushmessge.config.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.util.resources.cldr.ff.LocaleNames_ff;

/**
 * 推送工具
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

    public static int pushResult(JPushClient jPushClient, PushPayload pushPayload) {
        try {
            LOGGER.info("" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                return com.soft863.pushmessge.config.PushResult.PUSH_FAILURE;
            }
        } catch (APIConnectionException e) {
            // 容错
            e.printStackTrace();

        } catch (APIRequestException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new PushMessageException("基础框架异常", 11001, e);
        }
        return com.soft863.pushmessge.config.PushResult.PUSH_SUCCESS;
    }
}
