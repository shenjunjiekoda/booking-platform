package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 15:09
 */
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @GetMapping("/hi")
    public String test1() {
        return "hi~~~";
    }

    @GetMapping("/rest")
    public RestBody test2() {
        return RestBody.succeed("rest~~~");
    }

}
