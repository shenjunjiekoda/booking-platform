package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.service.JsoupService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 10:17
 */
@Slf4j
@Service
public class JsoupServiceImpl implements JsoupService {

    @Override
    public RestBody getBook() {
        Document document = null;
        try {
            document = Jsoup.connect("http://search.dangdang.com/").get();
        } catch (IOException e) {
            return RestBody.fail("Seach in the web cannot be used.Please contact admin!");
        }
        log.info("document.title:{}",document.title());
        Elements names = document.select(".name");

        return null;
    }

}
