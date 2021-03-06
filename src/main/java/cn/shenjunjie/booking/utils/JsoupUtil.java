package cn.shenjunjie.booking.utils;

import cn.shenjunjie.booking.common.exception.JsoupException;
import cn.shenjunjie.booking.entity.Book;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 11:01
 */
@Slf4j
public class JsoupUtil {
    @Value("${booking.url}")
    private static final String DANG_URL = "http://search.dangdang.com/";
    @Value("${booking.result-limit}")
    private static final Integer SEARCH_RESULTS = 10;

    private static final String PRIMARY_SEARCH_PREFIX = "?medium=01";
    private static final String FIX_SEARCH_PARAM = "&category_path=01.00.00.00.00.00";
    private static final String BOOKNAME_PARAM = "&key1=";
    private static final String AUTHOR_PARAM = "&key2=";
    private static final String PRESS_PARAM = "&key3=";
    private static final String ISBN_PARAM = "&key4=";
    private static final String TITLE = "title";
    private static final String HREF = "href";
    private static final String NAME_SELECTOR = ".name";
    private static final String DETAIL_SELECTOR = ".search_book_author";
    private static final String GBK = "GBK";
    private static final String ISBN = "ISBN";
    private static final String ISBN_PREFIX = "：";
    private static final String ISBN_PAGE_SELECTOR = ".key.clearfix";

    public static List<Book> getBookList(String bookNameKeyWord, String authorKeyWord, String pressKeyWord, String isbnKeyWord) {
        if (StringUtils.isEmpty(bookNameKeyWord) && StringUtils.isEmpty(authorKeyWord) && StringUtils.isEmpty(pressKeyWord) && StringUtils.isEmpty(isbnKeyWord)) {
            return null;
        }
        StringBuilder url = new StringBuilder();
        url.append(DANG_URL).append(PRIMARY_SEARCH_PREFIX).append(FIX_SEARCH_PARAM);
        List<Book> books = Lists.newArrayList();
        //encode the keyword
        if (!StringUtils.isEmpty(bookNameKeyWord)) {
            url.append(BOOKNAME_PARAM).append(getEncodeString(bookNameKeyWord));
        }
        if (!StringUtils.isEmpty(authorKeyWord)) {
            url.append(AUTHOR_PARAM).append(getEncodeString(authorKeyWord));
        }
        if (!StringUtils.isEmpty(pressKeyWord)) {
            url.append(PRESS_PARAM).append(getEncodeString(pressKeyWord));
        }
        if (!StringUtils.isEmpty(isbnKeyWord)) {
            url.append(ISBN_PARAM).append(getEncodeString(isbnKeyWord));
        }
        log.debug("search url:{}",url.toString());
        Document document = null;
        try {
            document = Jsoup.connect(url.toString()).get();
        } catch (IOException e) {
            throw new JsoupException(e.getMessage());
        }
        Elements names = document.select(NAME_SELECTOR);
        Elements detail = document.select(DETAIL_SELECTOR);
        int length = names.size();
        length = length > SEARCH_RESULTS ? SEARCH_RESULTS : length;
        for (int i = 0; i < length; i++) {
            Book record = new Book();
            Element nameElement = names.get(i);
            String name = nameElement.children().attr(TITLE).trim();
            if (name.indexOf(" ") != -1) {
                name = name.substring(0, name.indexOf(" "));
            }
            String bookUrl = nameElement.children().attr(HREF);
            if (!StringUtils.isEmpty(bookUrl)) {
                String isbn = getISBN(bookUrl);
                record.setIsbn(isbn);
            }
            Element detailElement = detail.get(i);
            if (detailElement.hasText()) {
                String author = "";
                try {
                    author = detailElement.childNode(0).childNode(0).attr(TITLE).trim();
                } catch (Exception e) {
                    continue;
                }
                if (detailElement.childNode(1).childNodes().size() > 0 && detailElement.childNode(1).childNode(0).toString().length() > 2) {
                    String publishedAt = detailElement.childNode(1).childNode(0).toString().substring(2);
                    if (Strings.isNotBlank(publishedAt)) {
                        try {
                            String[] ss = publishedAt.split("-");
                            publishedAt = ss[0] + "." + ss[1];
                        } catch (Exception e) {
                        }
                        record.setPublishedAt(publishedAt);
                    }
                }
                String press = detailElement.childNode(2).childNode(1).attr(TITLE);
                record.setName(name);
                record.setAuthor(author);
                record.setPress(press);
            }
            log.info("book:{}", record);
            if (recordIsRational(record)) {
                books.add(record);
            }
        }
        return books;
    }

    private static String getEncodeString(String s) {
        String encodeBookName;
        try {
            encodeBookName = URLEncoder.encode(s, GBK);
        } catch (UnsupportedEncodingException e) {
            throw new JsoupException(e.getMessage());
        }
        return encodeBookName;
    }



    public static List<Book> getBookListByIsbn(String isbn) {
        List<Book> books = Lists.newArrayList();
        //encode the keyword
        String encodeKeyword = getEncodeString(isbn);
        String url = DANG_URL.concat(PRIMARY_SEARCH_PREFIX).concat(FIX_SEARCH_PARAM).concat(ISBN_PARAM).concat(encodeKeyword);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new JsoupException(e.getMessage());
        }
        Elements names = document.select(NAME_SELECTOR);
        Elements detail = document.select(DETAIL_SELECTOR);
        int length = names.size();
        length = length > SEARCH_RESULTS ? SEARCH_RESULTS : length;
        for (int i = 0; i < length; i++) {
            Book record = new Book();
            Element nameElement = names.get(i);
            String name = nameElement.children().attr(TITLE).trim();
            if (name.indexOf(" ") != -1) {
                name = name.substring(0, name.indexOf(" "));
            }
            String bookUrl = nameElement.children().attr(HREF);
            if (!StringUtils.isEmpty(bookUrl)) {
                String newIsbn = getISBN(bookUrl);
                if (!isbn.equals(newIsbn)) {
                    continue;
                }
                record.setIsbn(isbn);
            }
            Element detailElement = detail.get(i);
            if (detailElement.hasText()) {
                String author = "";
                try {
                    author = detailElement.childNode(0).childNode(0).attr(TITLE).trim();
                } catch (Exception e) {
                    continue;
                }
                if (detailElement.childNode(1).childNodes().size() > 0 && detailElement.childNode(1).childNode(0).toString().length() > 2) {
                    String publishedAt = detailElement.childNode(1).childNode(0).toString().substring(2);
                    if (Strings.isNotBlank(publishedAt)) {
                        try {
                            String[] ss = publishedAt.split("-");
                            publishedAt = ss[0] + "." + ss[1].substring(1);
                        } catch (Exception e) {
                        }
                        record.setPublishedAt(publishedAt);
                    }
                }
                String press = detailElement.childNode(2).childNode(1).attr(TITLE);
                record.setName(name);
                record.setAuthor(author);
                record.setPress(press);
            }
            log.info("book:{}", record);
            if (recordIsRational(record)) {
                books.add(record);
            }
        }
        return books;
    }

    private static boolean recordIsRational(Book record) {
        if (StringUtils.isEmpty(record.getAuthor()) || StringUtils.isEmpty(record.getIsbn()) ||
                StringUtils.isEmpty(record.getName()) || StringUtils.isEmpty(record.getPress()) || record.getPublishedAt() == null) {
            return false;
        }
        return true;
    }

    private static String getISBN(String bookUrl) {
        Document bookDoc = null;
        try {
            bookDoc = Jsoup.connect(bookUrl).get();
        } catch (IOException e) {
            throw new JsoupException(e.getMessage());
        }
        Elements lis = bookDoc.select(ISBN_PAGE_SELECTOR);
        if (CollectionUtils.isEmpty(lis)) {
            return null;
        }
        String isbn = null;
        for (Node childNode : lis.get(0).childNodes()) {
            if (childNode.toString().contains(ISBN)) {
                String str = ((Element) childNode).text();
                if (str.length() > 2) {
                    isbn = str.substring(str.indexOf(ISBN_PREFIX) + 1);
                    return isbn;
                }
            }
        }
        return "";
    }


}
