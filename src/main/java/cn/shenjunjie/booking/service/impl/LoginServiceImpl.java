package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.LoginRequest;
import cn.shenjunjie.booking.dto.request.RegisterRequest;
import cn.shenjunjie.booking.dto.response.LoginResponse;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.repo.TeacherRepo;
import cn.shenjunjie.booking.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/15 8:54
 */

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private TeacherRepo teacherRepo;

    @Value("${shiro.login.expiredtime}")
    private Long expiredTime = 86400000L;

    @Override
    public RestBody login(LoginRequest request) {
        Subject subject = SecurityUtils.getSubject();
        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(request.getUsername(), request.getPassword());
        try {
            subject.login(token);
            //shiro登录态过期时间
            subject.getSession().setTimeout(expiredTime);
            Teacher principal = (Teacher) subject.getPrincipal();
            Session session = subject.getSession();
            LoginResponse response = new LoginResponse();
            response.setName(principal.getName())
                    .setToken(session.getId().toString())
                    .setEmail(principal.getEmail())
                    .setUuid(principal.getTeacherId())
                    .setTeacherId(principal.getTeacherId())
                    .setExpireTime(expiredTime / 24 / 60 / 60);
            return RestBody.succeed(response);
        } catch (UnknownAccountException e) {
            return RestBody.fail("Unknown Account");
        } catch (IncorrectCredentialsException e) {
            return RestBody.fail("Incorrect credential");
        }
    }

    @Override
    public RestBody register(RegisterRequest request) {
        Teacher teacher = teacherRepo.selectByName(request.getName());
        if (teacher != null) {
            return RestBody.fail("名字已存在");
        }
        teacher = teacherRepo.selectByTeacherId(request.getTeacherId());
        if (teacher != null) {
            return RestBody.fail("工号已存在");
        }
        teacher = teacherRepo.selectByEmail(request.getEmail());
        if (teacher != null) {
            return RestBody.fail("邮箱已存在");
        }
        teacherRepo.insertByRequest(request);
        return RestBody.succeed();
    }

    @Override
    public RestBody logout() {
        Subject subject = SecurityUtils.getSubject();
        log.info("logOut parms:{}", subject.getPrincipal());
        subject.logout();
        return RestBody.succeed();
    }

}
