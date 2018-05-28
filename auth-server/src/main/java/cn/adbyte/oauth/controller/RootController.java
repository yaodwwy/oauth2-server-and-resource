package cn.adbyte.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("")
public class RootController {

    @ResponseBody
    @RequestMapping("")
    public String root() {
        return "hello oauth！";
    }

    @RequestMapping("/login")
    public String login() {
        return "account/login";
    }
}