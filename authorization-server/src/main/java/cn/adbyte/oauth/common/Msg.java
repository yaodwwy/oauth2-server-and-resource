package cn.adbyte.oauth.common;

import java.io.Serializable;

public class Msg implements Serializable {

    public static final int SUCCESS=200;
    public static final int FAILED=201;
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}