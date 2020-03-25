package cn.shenjunjie.booking.service;


import cn.shenjunjie.booking.dto.request.GetCourseRequest;
import cn.shenjunjie.booking.dto.request.UpdateCourseRequest;
import cn.shenjunjie.booking.dto.response.GetCourseResponse;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface CourseService {

    List<GetCourseResponse> getCourses(GetCourseRequest request);

    List<String> getCourseByKeyword(String keyword);

    void addCourse(String name);

    void updateCourse(UpdateCourseRequest request);

}
