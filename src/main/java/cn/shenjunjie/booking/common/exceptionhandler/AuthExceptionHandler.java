package cn.shenjunjie.booking.common.exceptionhandler;

import cn.shenjunjie.booking.common.rest.RestBody;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 14:08
 */
@ControllerAdvice
@Slf4j
public class AuthExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public RestBody ErrorHandler(UnauthorizedException e) {
        return RestBody.fail("You are not authorized, Please Contact admin for permissions.");
    }

}
