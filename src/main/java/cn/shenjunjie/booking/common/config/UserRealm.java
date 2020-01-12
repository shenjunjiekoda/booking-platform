package cn.shenjunjie.booking.common.config;

import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.repo.TeacherRepo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/9 14:14
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private TeacherRepo teacherRepo;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("执行了=>授权doGetAuthorizationInfo principals:{}", JSON.toJSONString(principals));
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermission("user:add");
//        info.addRole("admin");
        return info;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("执行了=>认证doGetAuthenticationInfo token:{}", JSON.toJSONString(token));
        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
        Teacher teacher = teacherRepo.selectByTeacherId(userToken.getUsername());
        if (!userToken.getUsername().equals(teacher.getTeacherId())) {
            return null;
        }
        return new SimpleAuthenticationInfo(teacher, teacher.getPassword(), "");
    }
}
