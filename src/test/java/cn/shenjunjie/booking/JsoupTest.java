package cn.shenjunjie.booking;

import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.utils.JsoupUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 12:26
 */
public class JsoupTest extends BaseTest {


    @Test
    void test(){
        List<Book> books = JsoupUtil.getBookListByIsbn("9787302517597");
        for (Book book : books) {
            System.out.println("Book:" + book);
        }
    }

}
