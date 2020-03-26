package cn.shenjunjie.booking.utils;

import cn.shenjunjie.booking.entity.Teacher;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Objects;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/22 11:30
 */
public class UserUtil {


    public static Teacher getCurrentUser() {
        try{
            Subject subject = SecurityUtils.getSubject();
            Object principal = subject.getPrincipal();
            if (principal instanceof Teacher) {
                return (Teacher) principal;
            }
        }catch (Exception e){
        }
        return null;
    }

    public static String getCurrentUserName() {
        if (Objects.nonNull(getCurrentUser())) {
            try{
                return getCurrentUser().getName();
            }catch(Exception e){
                return "未知";
            }
        }
        return "未知";
    }

    public static boolean isAdmin() {
        if(Objects.nonNull(getCurrentUser())){
            return getCurrentUser().getIsadmin() == 1;
        }
        return false;
    }

}
