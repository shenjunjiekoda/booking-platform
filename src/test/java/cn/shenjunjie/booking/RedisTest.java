package cn.shenjunjie.booking;

import cn.shenjunjie.booking.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 8:09
 */
public class RedisTest extends BaseTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void testSet(){
       redisUtil.set("abc",123);
    }

    @Test
    void testGet() {
        System.out.println(redisUtil.get("aaa"));
    }

    @Test
    void test3(){
        redisUtil.set("aaa",123);
        System.out.println(redisUtil.get("aaa"));
    }

}
