package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.request.UpdateBookRequest;
import cn.shenjunjie.booking.dto.response.GetBooksResponse;
import cn.shenjunjie.booking.dto.response.PageBean;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface BookService {

    /**
     * 返回根据搜索条件得到的书籍列表
     * @param request
     * @return
     */
    PageBean<GetBooksResponse> getBooks(GetBooksRequest request);

    /**
     * 添加一本书籍
     * @param request
     */
    void addBook(AddBookRequest request);

    /**
     * 更新一本书籍
     * @param request
     */
    void updateBook(UpdateBookRequest request);

}
