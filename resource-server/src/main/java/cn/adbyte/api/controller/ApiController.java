package cn.adbyte.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiController {
    @ResponseBody
    @GetMapping("")
    public String apiRoot(){
        return "hello api！";
    }
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('api/auth')")
    @GetMapping("api/auth")
    public String api(){
        return "auth good!";
    }

}