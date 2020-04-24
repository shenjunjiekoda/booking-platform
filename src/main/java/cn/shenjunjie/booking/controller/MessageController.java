package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddMessageRequest;
import cn.shenjunjie.booking.dto.request.GetMessageRequest;
import cn.shenjunjie.booking.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 11:50
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    @GetMapping("/list")
    public RestBody getMessages(GetMessageRequest request) {
        log.debug("getMessages request:{}", request);
        return RestBody.succeed(messageService.getMessages(request));
    }

    @PostMapping("/read/{id}")
    public RestBody read(@PathVariable("id") Long id){
        log.debug("read id:{}", id);
        messageService.read(id);
        return RestBody.succeed();
    }

    @PostMapping("/readall")
    public RestBody readAll(){
        log.debug("readall");
        messageService.readAll();
        return RestBody.succeed();
    }

    @GetMapping("/unread/count")
    public RestBody getUnReadedCount() {
        log.debug("getUnReadedCount");
        return RestBody.succeed(messageService.getUnReadedCount());
    }

    @PostMapping("/add")
    public RestBody addMessage(@RequestBody @Valid AddMessageRequest request){
        log.debug("addMessage request:{}", request);
        return RestBody.succeed(messageService.addMsg(request));
    }

}
