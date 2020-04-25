package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.request.SearchBookRequest;
import cn.shenjunjie.booking.dto.request.UpdateBookRequest;
import cn.shenjunjie.booking.dto.response.GetBooksFromWebResponse;
import cn.shenjunjie.booking.dto.response.GetBooksResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.repo.BookRepo;
import cn.shenjunjie.booking.service.BookService;
import cn.shenjunjie.booking.utils.JsoupUtil;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class BookServiceImpl implements BookService {

    @Resource
    private BookRepo bookRepo;

    @Override
    public PageBean<GetBooksResponse> getBooks(GetBooksRequest request) {
        List<GetBooksResponse> booksResponseList = Lists.newArrayList();
        Page<Book> list = bookRepo.selectByGetBooksRequest(request);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(book -> {
                GetBooksResponse response = new GetBooksResponse();
                BeanUtils.copyProperties(book, response);
                booksResponseList.add(response);
            });
        }

        return new PageBean<>(request, booksResponseList, CollectionUtils.isEmpty(list) ? 0L : list.getTotal());
    }

    @Override
    public List<GetBooksFromWebResponse> getBooksFromWeb(SearchBookRequest request) {
        if (StringUtils.isEmpty(request) || (StringUtils.isEmpty(request.getBookName()) && StringUtils.isEmpty(request.getAuthor())) && StringUtils.isEmpty(request.getPress()) && StringUtils.isEmpty(request.getIsbn())) {
            return null;
        }
        List<GetBooksFromWebResponse> booksFromWebResponseList = Lists.newArrayList();
        List<Book> bookList = JsoupUtil.getBookList(request.getBookName(), request.getAuthor(), request.getPress(), request.getIsbn());
        if (!CollectionUtils.isEmpty(bookList)) {
            bookList.forEach(book -> {
                GetBooksFromWebResponse response = new GetBooksFromWebResponse();
                BeanUtils.copyProperties(book, response);
                booksFromWebResponseList.add(response);
            });
        }

        return booksFromWebResponseList;
    }

    @Override
    public List<Book> getSpecificBookByKeyword(String keyword) {
        List<Book> books = bookRepo.selectByPartName(keyword);
        if (!CollectionUtils.isEmpty(books)) {
            books = books.stream().limit(5).collect(Collectors.toList());
        }
        return books;
    }

    @Transactional
    @Override
    public RestBody addBook(AddBookRequest request) {
        Book book = bookRepo.selectByName(request.getName());
        if (book != null) {
            return RestBody.fail("书名已存在");
        }
        book = bookRepo.selectByISBN(request.getIsbn());
        if (book != null) {
            return RestBody.fail("ISBN号已存在");
        }
        bookRepo.insertByAddBookRequest(request);
        return RestBody.succeed();
    }

    @Transactional
    @Override
    public RestBody updateBook(UpdateBookRequest request) {
        String publishedAt = null;
        if (request.getPublishedAtMonth() != null && request.getPublishedAtMonth() != null) {
            publishedAt = request.getPublishedAtYear() + "." + request.getPublishedAtMonth();
            bookRepo.updateByUpdateBookRequest(request, publishedAt);
        } else if ((request.getPublishedAtYear() == null && request.getPublishedAtYear() != null) || (request.getPublishedAtYear() != null && request.getPublishedAtYear() == null)) {
            return RestBody.fail("出版年月必须同时不为空");
        }
        return RestBody.succeed();
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        bookRepo.deleteById(id);
    }

}
