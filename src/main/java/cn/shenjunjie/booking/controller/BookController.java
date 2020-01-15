package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private BookService bookService;

    @PostMapping("/list")
    public RestBody getBooks(GetBooksRequest request) {
        log.info("getBooks request:{}", request);
        return RestBody.succeed(bookService.getBooks(request));
    }

    @PostMapping("/add")
    public RestBody addBook(AddBookRequest request) {
        log.info("addBook request:{}", request);
        if(!StringUtils.isEmpty(request.getName()) || (!StringUtils.isEmpty(request.getName().trim()))) {
            return RestBody.fail("Book's name cannot be empty");
        }
        if(!StringUtils.isEmpty(request.getIsbn()) || (!StringUtils.isEmpty(request.getIsbn().trim()))) {
            return RestBody.fail("Book's isbn cannot be empty");
        }
        bookService.addBook(request);
        return RestBody.succeed();
    }

}
