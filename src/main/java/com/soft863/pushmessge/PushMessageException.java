package com.soft863.pushmessge;

/**
 * 更新前检查异常
 */
public class PushMessageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 1100;

    public PushMessageException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public PushMessageException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public PushMessageException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public PushMessageException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
