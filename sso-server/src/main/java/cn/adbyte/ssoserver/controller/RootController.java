package cn.adbyte.ssoserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class RootController {

    @ResponseBody
    @RequestMapping("")
    public String apiRoot(Principal user) {
        return "index";
    }

    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('api/auth')")
    @RequestMapping("api/auth")
    public String api() {
        return "auth good!";
    }

    @ResponseBody
    @RequestMapping("login")
    public Principal login(Principal user) {
        return user;
    }

    @ResponseBody
    @RequestMapping("user")
    public Principal user(Principal user) {
        if (user == null) {
            user = () -> "用户为空";
        }
        return user;
    }

}