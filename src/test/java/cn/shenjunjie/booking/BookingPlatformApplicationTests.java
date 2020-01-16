package cn.shenjunjie.booking;

import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.utils.JsoupUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class BookingPlatformApplicationTests {

    @Test
    void test1(){
        String isbn = "沈俊杰";
        List<Book> bookList = JsoupUtil.getBookList(isbn);
        for (Book book : bookList) {
            System.out.println(book);
        }
    }


    @Test
    void getBookNameList() {

        Document document = null;
        try {
            document = Jsoup.connect("http://search.dangdang.com/?key=9787111641247&act=input").get();
        } catch (IOException e) {
            System.out.println("Seach in the web cannot be used.Please contact admin!");
        }
        System.out.println("document.title: " + document.title());
        Elements names = document.select(".name");
        for (Element element : names) {
            System.out.println("book's name: " + element.childNode(0).attr("title"));
            String bookUrl = element.childNode(0).attr("href");
            Document bookDoc = null;
            try {
                bookDoc = Jsoup.connect(bookUrl).get();
            } catch (IOException e) {
                System.out.println("Seach in the web cannot be used.Please contact admin!");
            }
            Elements lis = bookDoc.select(".key.clearfix");
            lis.get(0).childNodes().stream().filter(li -> li.toString().indexOf("ISBN") != -1)
                    .forEach(li -> {
                String str = ((Element) li).text();
                if(str.length()>2) {
                    String isbn = str.substring(str.indexOf("：") + 1);
                    System.out.println(isbn);
                }
            });
            //        国际标准书号ISBN：9787111641247

        }
//        Elements detail = document.select(".search_book_author");
//        for (Element element : detail) {
//            System.out.println("book's author:" + element.childNode(0).childNode(0).attr("title"));
//            System.out.println("book's publishedAt:" + element.childNode(1).childNode(0));
//            System.out.println("book's press:" + element.childNode(2).childNode(1).attr("title"));
//
//        }
    }

}
