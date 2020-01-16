package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.response.GetCurProfileResponse;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.service.TeacherService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Override
    public GetCurProfileResponse getCurProfile() {
        Subject subject = SecurityUtils.getSubject();
        Teacher principal = (Teacher) subject.getPrincipal();
        if (Objects.isNull(principal)) {
            return null;
        }

        GetCurProfileResponse response = new GetCurProfileResponse();
        response.setTeacherId(principal.getTeacherId())
                .setName(principal.getName())
                .setEmail(principal.getEmail());
        return response;
    }

}
