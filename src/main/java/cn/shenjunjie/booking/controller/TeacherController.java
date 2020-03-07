package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.LoginRequest;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.repo.TeacherRepo;
import cn.shenjunjie.booking.service.LoginService;
import cn.shenjunjie.booking.service.TeacherService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;
    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public RestBody login(@RequestBody @Valid LoginRequest request) {
        log.info("login request:{}", request);
        return loginService.login(request);
    }

    @PostMapping("/logout")
    public RestBody logOut() {
        return loginService.logout();
    }

    @GetMapping("/tologin")
    public RestBody toLogin() {
        log.info("toLogin!");
        return RestBody.fail("You haven't signed in yet, Please login first!");
    }

    @GetMapping("/curprofile")
    public RestBody getCurProfile() {
        return RestBody.succeed(teacherService.getCurProfile());
    }

}
