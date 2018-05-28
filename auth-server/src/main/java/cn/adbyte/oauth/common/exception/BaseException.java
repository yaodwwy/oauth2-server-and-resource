package cn.adbyte.oauth.common.exception;

import cn.adbyte.oauth.common.Result;
import com.alibaba.fastjson.JSON;

public class BaseException extends RuntimeException {

    public BaseException(ErrorCode errorCode) {
        super(createFriendlyErrMsg(errorCode.des, errorCode.code));
    }

    public BaseException(String errorMsg, Number errorCode) {
        super(createFriendlyErrMsg(errorMsg, errorCode));
    }

    public BaseException(String errorMsg) {
        super(errorMsg);
    }

    private static String createFriendlyErrMsg(String msgBody, Number errorCode) {
        return JSON.toJSONString(new Result<String>(errorCode.intValue(), msgBody, null));
    }
}