package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.request.UpdateBookRequest;
import cn.shenjunjie.booking.dto.response.GetBooksFromWebResponse;
import cn.shenjunjie.booking.dto.response.GetBooksResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Book;

import java.util.List;

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
     * 返回从网上搜集的书籍列表
     * @param keyword
     * @return
     */
    List<GetBooksFromWebResponse> getBooksFromWeb(String keyword);

    /**
     * 通过关键词找到书名
     * @param keyword
     * @return
     */
    List<Book> getSpecificBookByKeyword(String keyword);

    /**
     * 添加一本书籍
     * @param request
     */
    RestBody addBook(AddBookRequest request);

    /**
     * 更新一本书籍
     * @param request
     */
    RestBody updateBook(UpdateBookRequest request);

    /**
     * 删除一本书籍
     * @param id
     */
    void deleteBook(Long id);
}
