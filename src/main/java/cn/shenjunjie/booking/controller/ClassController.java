package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddClassRequest;
import cn.shenjunjie.booking.dto.request.DeleteClassRequest;
import cn.shenjunjie.booking.dto.request.UpdateClassRequest;
import cn.shenjunjie.booking.service.ClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
@RequestMapping("/class")
public class ClassController {

    @Resource
    private ClassService classService;

    @GetMapping("/list/{id}")
    public RestBody getClasses(@PathVariable("id") Long id) {
        log.info("getClasses id:{}", id);
        return RestBody.succeed(classService.getClasses(id));
    }

    @GetMapping("/list/name/{keyword}")
    public RestBody getClassNameByKeyword(@PathVariable("keyword")String keyword){
        log.debug("getClassNameByKeyword keyword:{}",keyword);
        return RestBody.succeed(classService.getClassesByKeyword(keyword));
    }

    @PostMapping("/add")
    public RestBody addClass(@RequestBody @Valid AddClassRequest request) {
        log.info("addClass request:{}", request);
        classService.addClass(request);
        return RestBody.succeed();
    }

    @PostMapping("/update")
    public RestBody updateClass(@RequestBody @Valid UpdateClassRequest request) {
        log.info("addClass request:{}", request);
        classService.updateClass(request);
        return RestBody.succeed();
    }

    @PostMapping("/delete/{id}")
    public RestBody deleteClass(@PathVariable("id") Long id) {
        log.info("deleteClass request:{}", id);
        classService.deleteClass(id);
        return RestBody.succeed();
    }

}
