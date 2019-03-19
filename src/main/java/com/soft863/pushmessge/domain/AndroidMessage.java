package com.soft863.pushmessge.domain;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AndroidMessage
 * @Author
 * @Date 2019/3/19
 */
public class AndroidMessage {
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

    public AndroidMessage(List<String> tagsOrAlias, String alter, String title, String msgContent, Map<String, String> extras) {
        this.tagsOrAlias = tagsOrAlias;
        this.alter = alter;
        this.title = title;
        this.msgContent = msgContent;
        this.extras = extras;
    }

    public AndroidMessage(String alter, String title, String msgContent) {
        this.alter = alter;
        this.title = title;
        this.msgContent = msgContent;
    }

    public AndroidMessage(String alter, String title, String msgContent, Map<String, String> extras) {
        this.alter = alter;
        this.title = title;
        this.msgContent = msgContent;
        this.extras = extras;
    }

    public AndroidMessage(String alter, String title) {
        this.alter = alter;
        this.title = title;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
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

    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }
}
