package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.response.GetBooksResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.repo.BookRepo;
import cn.shenjunjie.booking.service.BookService;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    public void addBook(AddBookRequest request) {
        bookRepo.insertByAddBookRequest(request);
    }

}
