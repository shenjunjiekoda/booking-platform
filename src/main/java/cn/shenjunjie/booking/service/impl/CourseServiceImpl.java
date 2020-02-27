package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.GetCourseRequest;
import cn.shenjunjie.booking.dto.request.UpdateCourseRequest;
import cn.shenjunjie.booking.dto.response.GetCourseResponse;
import cn.shenjunjie.booking.entity.Course;
import cn.shenjunjie.booking.repo.CourseRepo;
import cn.shenjunjie.booking.service.CourseService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Resource
    private CourseRepo courseRepo;

    @Override
    public List<GetCourseResponse> getCourses(GetCourseRequest request) {
        List<GetCourseResponse> courseResponseList = Lists.newArrayList();
        List<Course> list = courseRepo.selectByPartName(request.getName());
        if (CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                GetCourseResponse response = new GetCourseResponse();
                BeanUtils.copyProperties(item, response);
                courseResponseList.add(response);
            });
        }
        return courseResponseList;
    }

    @Transactional
    @Override
    public void addCourse(String name) {
        courseRepo.insertByName(name);
    }

    @Transactional
    @Override
    public void updateCourse(UpdateCourseRequest request) {
        courseRepo.updateByIdAndName(request.getId(),request.getName());
    }
}
