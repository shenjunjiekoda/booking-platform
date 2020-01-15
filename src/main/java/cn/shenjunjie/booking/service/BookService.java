package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.response.GetBooksResponse;
import cn.shenjunjie.booking.dto.response.PageBean;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface BookService {

    PageBean<GetBooksResponse> getBooks(GetBooksRequest request);

    void addBook(AddBookRequest request);

}
