package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.annotation.AuditLog;
import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.*;
import cn.shenjunjie.booking.enums.OperationType;
import cn.shenjunjie.booking.service.PlanService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/plan")
public class PlanController {

    @Resource
    private PlanService planService;

    @GetMapping("/list")
    public RestBody listPlans(GetPlanRequest request) {
        log.info("listPlans request:{}",request);
        return RestBody.succeed(planService.getPlans(request));
    }

    @AuditLog(type = OperationType.ADD)
    @PostMapping("/add")
    public RestBody addPlan(@RequestBody @Valid AddPlanRequest request) {
        log.info("addPlan request:{}",request);
        return planService.addPlan(request);
    }

    @AuditLog(type = OperationType.UPDATE)
    @PutMapping("/update")
    public RestBody updatePlan(@RequestBody @Valid UpdatePlanRequest request){
        log.info("updatePlan request:{}",request);
        return planService.updatePlan(request);
    }

    @GetMapping("/book/list")
    public RestBody listPlanBooks(@Valid GetPlanBookRequest request) {
        log.info("listPlanBooks request:{}",request);
        return RestBody.succeed(planService.getPlanBooks(request));
    }

    @PostMapping("/book/add")
    public RestBody addPlanBook(@RequestBody @Valid AddPlanBookRequest request) {
        log.info("addPlanBook request:{}",request);
        return planService.addPlanBook(request);
    }

    @PutMapping("/book/update")
    public RestBody updatePlanBook(@RequestBody @Valid UpdatePlanBookRequest request){
        log.info("updatePlanBook request:{}",request);
        return planService.updatePlanBook(request);
    }

    @PostMapping("book/delete/{id}")
    public RestBody deletePlanBook(@PathVariable("id") Long id) {
        log.info("deletePlanBook request:{}",id);
        return planService.deletePlanBook(id);
    }

    @PostMapping("book/submit/{id}")
    public RestBody submitPlanBook(@PathVariable("id") Long id) {
        log.info("submitPlanBook request:{}",id);
        return planService.submitPlanBook(id);
    }

}
