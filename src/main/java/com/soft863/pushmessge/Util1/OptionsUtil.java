package com.soft863.pushmessge.Util1;

import cn.jpush.api.push.model.Options;

/**
 * @ClassName: OptionsUtil
 * @Author
 * @Date 2019/3/18 0018
 */
public class OptionsUtil {
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
}
