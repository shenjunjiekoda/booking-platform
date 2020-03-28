package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 8:41
 */

@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private ConfigService configService;
    public static final String ADMIN = "admin";


    @RequiresRoles({ADMIN})
    @GetMapping("/email/get")
    public RestBody getEmailConfig() {
        log.debug("getEmailConfig");
        return RestBody.succeed(configService.getEmailConfig());
    }

    @RequiresRoles({ADMIN})
    @PostMapping("/email/switch")
    public RestBody switchEmailConfig(){
        log.info("switchEmailConfig");
        configService.switchEmailConfig();
        return RestBody.succeed();
    }

    @RequiresRoles({ADMIN})
    @PostMapping("/book/update")
    public RestBody updateBooks() {
        log.info("updateBooks");
        configService.updateBooks();
        return RestBody.succeed();
    }

    @RequiresRoles({ADMIN})
    @PostMapping("/book/schedule")
    public RestBody scheduledUpdateBooks(){
        log.info("scheduledUpdateBooks");
        configService.scheduleUpdateBooks();
        return RestBody.succeed();
    }

    @RequiresRoles({ADMIN})
    @GetMapping("/book/get")
    public RestBody getBookScheduleStatus(){
        log.info("stopScheduled");
        return RestBody.succeed(configService.getBookScheduleStatus());
    }

    @RequiresRoles({ADMIN})
    @PostMapping("/book/stop")
    public RestBody stopScheduled(){
        log.info("stopScheduled");
        configService.stopScheduled();
        return RestBody.succeed();
    }

}
