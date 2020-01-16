package cn.shenjunjie.booking.utils;

import cn.shenjunjie.booking.common.exception.JsoupException;
import cn.shenjunjie.booking.entity.Book;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 11:01
 */
@Slf4j
public class JsoupUtil {

    private static final String DANG_URL = "http://search.dangdang.com/";
    private static final String TITLE = "title";
    private static final String HREF = "href";
    private static final String NAME_SELECTOR = ".name";
    private static final String DETAIL_SELECTOR = ".search_book_author";
    private static final String GBK = "GBK";

    public static List<Book> getBookList(String keyword) {
        List<Book> books = Lists.newArrayList();

        String encodeKeyword =null;
        try {
            encodeKeyword = URLEncoder.encode(keyword, GBK);
        } catch (UnsupportedEncodingException e) {
            throw new JsoupException(e.getMessage());
        }
        String url = DANG_URL.concat("?key=") + encodeKeyword;
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new JsoupException(e.getMessage());
        }
        log.info("document.title: " + document.title());
        Elements names = document.select(NAME_SELECTOR);
        Elements detail = document.select(DETAIL_SELECTOR);
        int length = names.size();
        for (int i = 0; i < length; i++) {
            Book record = new Book();
            Element nameElement = names.get(i);
            String name = nameElement.children().attr(TITLE);
            String bookUrl = nameElement.children().attr(HREF);
            if(!StringUtils.isEmpty(bookUrl)) {
                String isbn = getISBN(bookUrl);
                record.setIsbn(isbn);
            }
            Element detailElement = detail.get(i);
            if (detailElement.hasText()) {
                String author = detailElement.childNode(0).childNode(0).attr(TITLE);
                if (detailElement.childNode(1).childNodes().size() > 0 && detailElement.childNode(1).childNode(0).toString().length() > 2) {
                    String publishedAt = detailElement.childNode(1).childNode(0).toString().substring(2);
                    record.setPublishedAt(publishedAt);
                }
                String press = detailElement.childNode(2).childNode(1).attr(TITLE);
                record.setName(name);
                record.setAuthor(author);
                record.setPress(press);
            }
            log.info("book:{}", record);
            books.add(record);
        }
        return books;
    }

    private static String getISBN(String bookUrl) {
        Document bookDoc = null;
        try {
            bookDoc = Jsoup.connect(bookUrl).get();
        } catch (IOException e) {
            throw new JsoupException(e.getMessage());
        }
        Elements lis = bookDoc.select(".key.clearfix");
        if(CollectionUtils.isEmpty(lis)) {
            return null;
        }
        String isbn = null;
        for (Node childNode : lis.get(0).childNodes()) {
            if(childNode.toString().contains("ISBN")) {
                String str = ((Element)childNode).text();
                if(str.length()>2) {
                    isbn = str.substring(str.indexOf("：") + 1);
                    return isbn;
                }
            }
        }
        return null;
    }


}
