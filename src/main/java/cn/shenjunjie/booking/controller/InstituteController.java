package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddInstituteRequest;
import cn.shenjunjie.booking.dto.request.UpdateInstituteRequest;
import cn.shenjunjie.booking.service.InstituteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:46
 */
@Slf4j
@RestController
@RequestMapping("/institute")
public class InstituteController {

    @Resource
    private InstituteService instituteService;

    @GetMapping("/list")
    public RestBody getInstitutes() {
        log.info("getInstitutes request:{}");
        return RestBody.succeed(instituteService.getInstitutes());
    }

    @GetMapping("list/class/{classId}")
    public RestBody getInstituteByClassId(@PathVariable("classId") Long classId) {
        log.info("getInstituteByClassId classId:{}", classId);
        return RestBody.succeed(instituteService.getInstituteByClassId(classId));
    }

    @PostMapping("/add")
    public RestBody addInstitute(@RequestBody @Valid AddInstituteRequest request) {
        log.info("addInstitute request:{}", request);
        instituteService.addInstitute(request);
        return RestBody.succeed();
    }

    @PostMapping("/update")
    public RestBody updateInstitute(@RequestBody @Valid UpdateInstituteRequest request) {
        log.info("updateInstitute request:{}", request);
        instituteService.updateInstitute(request);
        return RestBody.succeed();
    }

}
