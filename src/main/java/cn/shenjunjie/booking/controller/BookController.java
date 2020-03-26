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

import javax.annotation.Resource;
import javax.validation.Valid;
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

    @Resource
    private BookService bookService;

    @GetMapping("/list")
    public RestBody getBooks(GetBooksRequest request) {
        log.info("getBooks request:{}", request);
        return RestBody.succeed(bookService.getBooks(request));
    }

    @GetMapping("/search/{keyword}")
    public RestBody getBooksFromWeb(@PathVariable("keyword") String keyword) {
        log.info("getBooksFromWeb keyword:{}", keyword);
        return RestBody.succeed(bookService.getBooksFromWeb(keyword));
    }

    @GetMapping("/list/{keyword}")
    public RestBody getSpecificBookByKeyword(@PathVariable("keyword") String keyword) {
        log.debug("getSpecificBookByKeyword keyword:{}", keyword);
        return RestBody.succeed(bookService.getSpecificBookByKeyword(keyword));
    }

    @PostMapping("/add")
    public RestBody addBook(@RequestBody @Valid AddBookRequest request) {
        log.info("addBook request:{}", request);
        return bookService.addBook(request);
    }

    @PutMapping("/update")
    public RestBody updateBook(@RequestBody @Valid UpdateBookRequest request) {
        log.info("updateBook request:{}", request);
        bookService.updateBook(request);
        return RestBody.succeed();
    }

    @PostMapping("/delete/{id}")
    public RestBody deleteBook(@PathVariable("id") Long id) {
        log.info("deleteBook request:{}", id);
        bookService.deleteBook(id);
        return RestBody.succeed();
    }
}
