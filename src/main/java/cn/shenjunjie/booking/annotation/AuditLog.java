package cn.shenjunjie.booking.annotation;


import cn.shenjunjie.booking.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/19 15:00
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {


    /**
     * 操作类型
     * @return
     */
    OperationType type() default OperationType.UNKNOWN;

}
