package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddInstituteRequest;
import cn.shenjunjie.booking.service.InstituteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:46
 */
@Slf4j
@RestController
@RequestMapping("/institute")
public class InstituteController {

    @Autowired
    private InstituteService instituteService;

    @PostMapping("/list")
    public RestBody getInstitutes() {
        log.info("getInstitutes request:{}");
        return RestBody.succeed(instituteService.getInstitutes());
    }

    @PostMapping("/add")
    public RestBody addInstitute(@RequestBody AddInstituteRequest request) {
        log.info("addInstitute request:{}", request);
        if(Objects.isNull(request)) {
            return RestBody.fail("Please enter a correct name");
        }
        if (StringUtils.isEmpty(request.getName()) && StringUtils.isEmpty(request.getName().trim())) {
            return RestBody.fail("Name should not be empty!");
        }
        instituteService.addInstitute(request);
        return RestBody.succeed();
    }

}
