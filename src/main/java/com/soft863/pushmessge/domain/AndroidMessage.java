package com.soft863.pushmessge.domain;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AndroidMessage
 * @Author
 * @Date 2019/3/19
 */
public class AndroidMessage {
    private List<String> tagsOrAlias;
    private String alter;
    private String title;
    private Map<String,String> extras;

    public AndroidMessage(List<String> tagsOrAlias, String alter, String title, Map<String, String> extras) {
        this.tagsOrAlias = tagsOrAlias;
        this.alter = alter;
        this.title = title;
        this.extras = extras;
    }

    public AndroidMessage(String alter, String title, Map<String, String> extras) {
        this.alter = alter;
        this.title = title;
        this.extras = extras;
    }

    public AndroidMessage(String alter, String title) {
        this.alter = alter;
        this.title = title;
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
