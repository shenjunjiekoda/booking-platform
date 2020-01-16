package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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
        if(!CollectionUtils.isEmpty(list)) {
            list.forEach(book -> {
                GetBooksResponse response = new GetBooksResponse();
                BeanUtils.copyProperties(book, response);
                booksResponseList.add(response);
            });
        }

        return new PageBean<>(request, booksResponseList, CollectionUtils.isEmpty(list) ? 0L : list.getTotal());
    }

    @Override
    public List<GetBooksFromWebResponse> getBooksFromWeb(String keyword) {
        if(StringUtils.isEmpty(keyword) || StringUtils.isEmpty(keyword.trim())) {
            return null;
        }
        List<GetBooksFromWebResponse> booksFromWebResponseList = Lists.newArrayList();
        List<Book> bookList = JsoupUtil.getBookList(keyword);
        if(!CollectionUtils.isEmpty(bookList)){
            bookList.forEach( book -> {
                GetBooksFromWebResponse response = new GetBooksFromWebResponse();
                BeanUtils.copyProperties(book, response);
                booksFromWebResponseList.add(response);
            });
        }

        return booksFromWebResponseList;
    }

    @Override
    public void addBook(AddBookRequest request) {
        bookRepo.insertByAddBookRequest(request);
    }

    @Override
    public void updateBook(UpdateBookRequest request) {
        bookRepo.updateByUpdateBookRequest(request);
    }

}
