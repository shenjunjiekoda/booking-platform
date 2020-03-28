package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.SetSubscribeClassRequest;
import cn.shenjunjie.booking.dto.request.GetSubscribeLogRequest;
import cn.shenjunjie.booking.service.SubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:15
 */
@RestController
@Slf4j
@RequestMapping("/subscribe")
public class SubscribeController {

    @Resource
    private SubscribeService subscribeService;

    @GetMapping("/log/list")
    public RestBody getSubscribeLog(GetSubscribeLogRequest request) {
        log.debug("getSubscribeLog request:{}",request);
        return RestBody.succeed(subscribeService.getMsg(request));
    }

    @PostMapping("/class/set")
    public RestBody setSubscribeClasses(@RequestBody SetSubscribeClassRequest request){
        log.info("setSubscribeClasses request:{}",request);
        subscribeService.setSubscribeClasses(request);
        return RestBody.succeed();
    }

    @GetMapping("/class/list")
    public RestBody getSubscribeClasses() {
        log.debug("getSubscribeClasses");
        return RestBody.succeed(subscribeService.getSubscribeClasses());
    }

}
