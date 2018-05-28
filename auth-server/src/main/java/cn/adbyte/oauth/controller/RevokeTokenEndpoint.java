package cn.adbyte.oauth.controller;

import cn.adbyte.oauth.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FrameworkEndpoint
public class RevokeTokenEndpoint {
    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @RequestMapping(value = "/oauth/token", method = RequestMethod.DELETE)
    public @ResponseBody
    Result revokeToken(String access_token) {
        Result msg=null;
        if (consumerTokenServices.revokeToken(access_token)) {
            msg = Result.success("注销成功");
        }else {
            msg = Result.failed("注销失败");
        }
        return msg;
    }
}