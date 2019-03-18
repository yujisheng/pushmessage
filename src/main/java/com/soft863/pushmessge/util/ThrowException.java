package com.soft863.pushmessge.util;

import cn.jiguang.common.utils.StringUtils;

import java.util.List;


/**
 * 异常抛出
 *
 * @ClassName ThrowException
 * @Author
 * @Date 2019/3/18 0018
 */
public class ThrowException {
    public static void aliasOrTagsException(List<String> aliasOrTags){
        if(aliasOrTags.isEmpty()){
            throw new PushMessageException("根据别名或者tag推送消息是,别名或者tag“aliasOrTags”不能为空", 110101);
        }
    }

    public static void messageContentException(String msgContent){
        if(StringUtils.isEmpty(msgContent)){
            throw new PushMessageException("推送的消息内容“msgContent”不能为空", 110102);
        }
    }

    public static void notificationTitleException(String notificationTitle){
        if (StringUtils.isEmpty(notificationTitle)) {
            throw new PushMessageException("推送的通知主题“notificationTitle”不能为空", 110103);
        }
    }
}
