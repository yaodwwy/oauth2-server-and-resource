package cn.adbyte.oauth.common;

import java.io.Serializable;

public class Result<T> implements Serializable {

    public static final int SUCCESS = 0;    // 成功：零和正整数
    public static final int FAILED = -1;    // 失败：负整数

    private int code;
    private int count;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {

        this(code, 0, msg, data);
    }

    public Result(int code, int count, String msg, T data) {

        this.code = code;
        this.count = count;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // success
    public static Result success() {

        return new Result(SUCCESS, "", null);
    }

    public static Result success(String msg) {

        return new Result(SUCCESS, msg, null);
    }

    public static <S> Result<S> success(String msg, S data) {

        return new Result<>(SUCCESS, msg, data);
    }

    public static <S> Result<S> success(int count, S data) {

        return new Result<>(SUCCESS, count, "", data);
    }

    public static <S> Result<S> success(int count, String msg, S data) {

        return new Result<>(SUCCESS, count, msg, data);
    }

    // failed
    public static Result failed() {

        return new Result(FAILED, "", null);
    }

    public static Result failed(String msg) {

        return new Result(FAILED, msg, null);
    }

    public static <S> Result<S> failed(String msg, S data) {

        return new Result<>(FAILED, msg, data);
    }

    public static <S> Result<S> failed(int count, S data) {

        return new Result<>(FAILED, count, "", data);
    }

    public static <S> Result<S> failed(int count, String msg, S data) {

        return new Result<>(FAILED, count, msg, data);
    }
}
