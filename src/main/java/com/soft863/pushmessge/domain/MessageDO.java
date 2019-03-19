package com.soft863.pushmessge.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 推送的消息体
 * @ClassName MessageDO
 * @Author
 * @Date 2019/3/18
 */
public class MessageDO implements Serializable {

    private List<String> tagsOrAlias;
    private String alter;
    private String title;
    private String msgContent;
    private Map<String,String> extras;
    private String extraKey;
    private String extraValue;

    public MessageDO() {
    }

    public MessageDO(List<String> tagsOrAlias, String alter, String title, Map<String, String> extras) {
        this.tagsOrAlias = tagsOrAlias;
        this.alter = alter;
        this.title = title;
        this.extras = extras;
    }


    public MessageDO(String alter, String title, Map<String, String> extras) {
        this.alter = alter;
        this.title = title;
        this.extras = extras;
    }

    public MessageDO(List<String> tagsOrAlias, String alter, String msgContent, String extraKey, String extraValue) {
        this.tagsOrAlias = tagsOrAlias;
        this.alter = alter;
        this.msgContent = msgContent;
        this.extraKey = extraKey;
        this.extraValue = extraValue;
    }

    public MessageDO(String msgContent, String extraKey, String extraValue) {
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

    @Override
    public String toString() {
        return "MessageDO{" +
                "tagsOrAlias=" + tagsOrAlias +
                ", alter='" + alter + '\'' +
                ", title='" + title + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", extras=" + extras +
                ", extraKey='" + extraKey + '\'' +
                ", extraValue='" + extraValue + '\'' +
                '}';
    }
}
