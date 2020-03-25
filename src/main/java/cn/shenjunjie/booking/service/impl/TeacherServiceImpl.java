package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.response.GetCurProfileResponse;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.repo.TeacherRepo;
import cn.shenjunjie.booking.service.TeacherService;
import org.apache.commons.compress.utils.Lists;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private TeacherRepo teacherRepo;

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

    @Override
    public List<String> getTeacherNameByKeyword(String keyword) {
        List<String> names = Lists.newArrayList();
        List<Teacher> teachers = teacherRepo.selectByPartName(keyword);
        if (!CollectionUtils.isEmpty(teachers)) {
            names = teachers.stream().map(Teacher::getName).collect(Collectors.toList());
        }
        return names;
    }

}
