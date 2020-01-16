package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddClassRequest;
import cn.shenjunjie.booking.dto.request.DeleteClassRequest;
import cn.shenjunjie.booking.dto.request.UpdateClassRequest;
import cn.shenjunjie.booking.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping("/list/{id}")
    public RestBody getClasses(@PathVariable("id") Long id) {
        log.info("getClasses id:{}", id);
        classService.getClasses(id);
        return RestBody.succeed();
    }

    @PostMapping("/add")
    public RestBody addClass(@RequestBody AddClassRequest request) {
        log.info("addClass request:{}", request);
        classService.addClass(request);
        return RestBody.succeed();
    }

    @PostMapping("/update")
    public RestBody updateClass(@RequestBody UpdateClassRequest request) {
        log.info("addClass request:{}", request);
        classService.updateClass(request);
        return RestBody.succeed();
    }

    @PostMapping("/delete")
    public RestBody deleteClass(@RequestBody DeleteClassRequest request) {
        log.info("deleteClass request:{}", request);
        classService.deleteClass(request);
        return RestBody.succeed();
    }

}
