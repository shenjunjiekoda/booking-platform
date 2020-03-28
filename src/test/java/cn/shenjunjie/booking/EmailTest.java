package cn.shenjunjie.booking;

import cn.shenjunjie.booking.entity.Message;
import cn.shenjunjie.booking.utils.EmailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 8:09
 */
public class EmailTest extends BaseTest {

    @Autowired
    private EmailUtil emailUtil;

    @Test
    void testMail(){
        Message message = new Message();
        message.setFrom(1L);
        message.setTo(2L);
        message.setData("测试邮件！");
        emailUtil.sendMail(message);
    }
}
