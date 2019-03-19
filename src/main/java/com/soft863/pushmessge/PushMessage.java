package com.soft863.pushmessge;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.soft863.pushmessge.config.HttpStatus;
import com.soft863.pushmessge.config.SendPushResult;
import com.soft863.pushmessge.domain.AndroidMessage;
import com.soft863.pushmessge.domain.IosMessage;
import com.soft863.pushmessge.util.PushPayloadUtill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @ClassName PushMessage
 * @Author
 * @Date 2019/3/18
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
     * 向android所有用户推送消息
     * @param androidMessage
     * @return
     */
    public int pushMessageToAndroidAll(AndroidMessage androidMessage) throws APIConnectionException, APIRequestException {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        // 推送结果
        int result = sendPushMessage(jPushClient, PushPayloadUtill.pushMessageToAndroidAll(androidMessage, production, sendno, timeToLive));
        return  result;
    }

    /**
     * 根据tags向android用户推送消息
     * @param androidMessage
     * @return
     */
    public int pushMessageToAndroidByTags(AndroidMessage androidMessage) throws APIConnectionException, APIRequestException {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        // 推送结果
        int result = sendPushMessage(jPushClient, PushPayloadUtill.pushMessageToAndroidByTags(androidMessage, production, sendno, timeToLive));
        return  result;
    }

    /**
     * 根据alias向android用户推送消息
     * @param androidMessag
     * @return
     */
    public int pushMessageToAndroidByAlias(AndroidMessage androidMessag) throws APIConnectionException, APIRequestException {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        // 推送结果
        int result = sendPushMessage(jPushClient, PushPayloadUtill.pushMessageToAndroidByAlias(androidMessag, production, sendno, timeToLive));
        return  result;
    }

    /**
     * 向ios所有用户推送消息
     * @param iosMessage
     * @return
     */
    public int pushMessageToIosAll(IosMessage iosMessage) throws APIConnectionException, APIRequestException {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        int result = sendPushMessage(jPushClient, PushPayloadUtill.pushMessageToIosAll(iosMessage, badge, sound, production, sendno, timeToLive));
        return result;
    }

    /**
     * 根据tags向ios用户推送消息
     * @param iosMessage
     * @return
     */
    public int pushMessageToIosByTags(IosMessage iosMessage) throws APIConnectionException, APIRequestException {
        JPushClient jPushClient = new JPushClient(masterSecret, appKey);
        int result = sendPushMessage(jPushClient, PushPayloadUtill.pushMessageToIosAllByTags(iosMessage, badge, sound, production, sendno, timeToLive));
        return result;
    }


    /**
     * 推送消息
     * @param jPushClient
     * @param pushPayload
     * @return
     */
    private int sendPushMessage(JPushClient jPushClient, PushPayload pushPayload) throws APIConnectionException, APIRequestException {
        LOGGER.info("===========sendPush==================");
            LOGGER.info("消息推送前" + pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            LOGGER.info("消息推送后" + pushResult);
            if (pushResult.getResponseCode() == HttpStatus.SUCCESS) {
                return SendPushResult.PUSH_SUCCESS;
            }
        return SendPushResult.PUSH_FAILURE;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setSendno(int sendno) {
        this.sendno = sendno;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
