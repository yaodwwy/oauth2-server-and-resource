package cn.adbyte.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class RootController {

    @ResponseBody
    @GetMapping("")
    public String apiRoot() {
        return "hello api！";
    }

    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('api/auth')")
    @GetMapping("api/auth")
    public String api() {
        return "auth good!";
    }

    @ResponseBody
//    @PreAuthorize("hasAnyAuthority('api/auth')")
    @GetMapping("hi")
    public String hi() {
        return "hi!";
    }

    @RequestMapping("user")
    public Principal user(Principal user) {
        if (user == null) {
            user = () -> "用户为空";
        }
        return user;
    }

}