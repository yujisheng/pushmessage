package com.soft863.pushmessge.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 推送的消息体
 * @ClassName AllMessage
 * @Author
 * @Date 2019/3/18
 */
public class AllMessage{

    /**
     * tag或者alias标识
     */
    private List<String> tagsOrAlias;
    /**
     * 推送通知
     */
    private String alter;
    /**
     * 推送主题
     */
    private String title;
    /**
     * 推送消息的内容
     */
    private String msgContent;
    /**
     * 推送额外信息
     */
    private Map<String,String> extras;
    /**
     * 推送额外功能的key
     */
    private String extraKey;
    /**
     * 推送额外功能的key
     */
    private String extraValue;

    public AllMessage(List<String> tagsOrAlias, String alter, String title, String msgContent, Map<String, String> extras, String extraKey, String extraValue) {
        this.tagsOrAlias = tagsOrAlias;
        this.alter = alter;
        this.title = title;
        this.msgContent = msgContent;
        this.extras = extras;
        this.extraKey = extraKey;
        this.extraValue = extraValue;
    }

    public AllMessage(List<String> tagsOrAlias, String alter, String title, String msgContent, String extraKey, String extraValue) {
        this.tagsOrAlias = tagsOrAlias;
        this.alter = alter;
        this.title = title;
        this.msgContent = msgContent;
        this.extraKey = extraKey;
        this.extraValue = extraValue;
    }

    public AllMessage(String alter, String title, String msgContent, Map<String, String> extras, String extraKey, String extraValue) {
        this.alter = alter;
        this.title = title;
        this.msgContent = msgContent;
        this.extras = extras;
        this.extraKey = extraKey;
        this.extraValue = extraValue;
    }

    public AllMessage(String alter, String msgContent, String extraKey, String extraValue) {
        this.alter = alter;
        this.msgContent = msgContent;
        this.extraKey = extraKey;
        this.extraValue = extraValue;
    }



    public List<String> getTagsOrAlias() {
        return tagsOrAlias;
    }

    public void setTagsOrAlias(List<String> tagsOrAlias) {
        this.tagsOrAlias = tagsOrAlias;
    }

    public String getAlter() {
        return alter;
    }

    public void setAlter(String alter) {
        this.alter = alter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public String getExtraKey() {
        return extraKey;
    }

    public void setExtraKey(String extraKey) {
        this.extraKey = extraKey;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }
}
