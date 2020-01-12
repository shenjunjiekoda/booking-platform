package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
public class LoginController {

    @GetMapping("test1")
    public String test1() {
        return "hello~~~";
    }

    @GetMapping("/login")
    public RestBody login(String username, String password) {
        if (Objects.isNull(username)) {
            return RestBody.fail("Empty Account");
        }
        log.info("login username:{},password:{}", username, password);
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        log.info("token:{}", JSON.toJSONString(token));
        try {
            subject.login(token);
            return RestBody.succeed("login success");
        } catch (UnknownAccountException e) {
            return RestBody.fail("Unknown Account");
        } catch (IncorrectCredentialsException e) {
            return RestBody.fail("Incorrect credential");
        }
    }

    @PostMapping("/logout")
    public RestBody logOut() {
        Subject subject = SecurityUtils.getSubject();
        log.info("logOut parms:{}", subject.getPrincipal());
        subject.logout();
        return RestBody.succeed("Log out.");
    }

    @GetMapping("/tologin")
    public RestBody toLogin() {
        log.info("toLogin!");
        return RestBody.fail("You haven't signed in yet, Please login first!");
    }

}
