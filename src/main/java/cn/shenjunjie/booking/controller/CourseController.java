package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.GetCourseRequest;
import cn.shenjunjie.booking.dto.request.UpdateCourseRequest;
import cn.shenjunjie.booking.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
@RequestMapping("/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    @GetMapping("/list")
    public RestBody listCourses(GetCourseRequest request) {
        log.info("listCourses request:{}",request);
        return RestBody.succeed(courseService.getCourses(request));
    }

    @PostMapping("/add/{name}")
    public RestBody addCourse(@PathVariable("name") String name) {
        log.info("addCourse request:{}",name);
        courseService.addCourse(name);
        return RestBody.succeed();
    }

    @PostMapping("/update")
    public RestBody updateCourse(UpdateCourseRequest request) {
        log.info("updateCourse request:{}",request);
        courseService.updateCourse(request);
        return RestBody.succeed();
    }

}
