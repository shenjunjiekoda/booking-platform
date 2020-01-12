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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public RestBody login(String username, String password, HttpServletRequest request) {
        log.info("login username:{},password:{}",username,password);
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        log.info("token:{}", JSON.toJSONString(token));
        try{
            subject.login(token);
            return RestBody.succeed("login success");
        }catch (UnknownAccountException e) {
            return RestBody.fail("Unknown Account");
        }catch (IncorrectCredentialsException e) {
            return RestBody.fail("Incorrect credential");
        }
    }

    @GetMapping("/tologin")
    public RestBody toLogin(){
        log.info("toLogin!");
        return RestBody.fail("You need to login , Please try to clean the cookie and try again!");
    }

}
