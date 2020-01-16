package cn.shenjunjie.booking.controller;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.request.UpdateBookRequest;
import cn.shenjunjie.booking.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @GetMapping("/search/{keyword}")
    public RestBody getBooksFromWeb(@PathVariable("keyword") String keyword) {
        log.info("getBooksFromWeb keyword:{}", keyword);
        return RestBody.succeed(bookService.getBooksFromWeb(keyword));
    }

    @PostMapping("/add")
    public RestBody addBook(AddBookRequest request) {
        log.info("addBook request:{}", request);
        if (Objects.isNull(request)) {
            return RestBody.fail("Empty request");
        }
        if (!StringUtils.isEmpty(request.getName()) || (!StringUtils.isEmpty(request.getName().trim()))) {
            return RestBody.fail("Book's name cannot be empty");
        }
        if (!StringUtils.isEmpty(request.getIsbn()) || (!StringUtils.isEmpty(request.getIsbn().trim()))) {
            return RestBody.fail("Book's isbn cannot be empty");
        }
        bookService.addBook(request);
        return RestBody.succeed();
    }

    //todo 之后还要考虑如果该书籍在订单中，是否变更之前加一层寻找是否有在订单中书籍。
    @PostMapping("/update")
    public RestBody updateBook(@RequestBody UpdateBookRequest request) {
        log.info("updateBook request:{}", request);
        if (Objects.isNull(request)) {
            return RestBody.fail("Empty request");
        }
        if (Objects.isNull(request.getId())) {
            return RestBody.fail("Empty book id!");
        }
        bookService.updateBook(request);
        return RestBody.succeed();
    }
}
