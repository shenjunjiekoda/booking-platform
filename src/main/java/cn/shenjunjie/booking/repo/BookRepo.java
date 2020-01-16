package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.BookMapper;
import cn.shenjunjie.booking.dto.request.AddBookRequest;
import cn.shenjunjie.booking.dto.request.GetBooksRequest;
import cn.shenjunjie.booking.dto.request.UpdateBookRequest;
import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.entity.BookExample;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class BookRepo {

    @Resource
    private BookMapper bookMapper;

    public Page<Book> selectByGetBooksRequest(GetBooksRequest request) {
        return selectByGetBooksRequest(request, true);
    }

    public Page<Book> selectByGetBooksRequest(GetBooksRequest request, boolean pageAble) {
        if (pageAble) {
            PageHelper.startPage(request.getPage(), request.getLimit());
        }
        BookExample example = new BookExample();
        BookExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(request.getName())) {
            criteria.andNameLike("%".concat(request.getName()).concat("%"));
        }
        if (!StringUtils.isEmpty(request.getIsbn())) {
            criteria.andIsbnLike("%".concat(request.getIsbn()).concat("%"));
        }
        if (!StringUtils.isEmpty(request.getAuthor())) {
            criteria.andAuthorLike("%".concat(request.getAuthor()).concat("%"));
        }
        if (!StringUtils.isEmpty(request.getPress())) {
            criteria.andPressLike("%".concat(request.getPress()).concat("%"));
        }
        return (Page<Book>) bookMapper.selectByExample(example);
    }

    public void insertByAddBookRequest(AddBookRequest request) {
        Book record = new Book();
        record.setName(request.getName());
        record.setIsbn(request.getIsbn());
        record.setAuthor(request.getAuthor());
        record.setEdition(request.getEdition());
        record.setPress(request.getPress());
        record.setPublishedAt(request.getPublishedAt());
        record.setRemark(request.getRemark());
        bookMapper.insertSelective(record);
    }

    public void updateByUpdateBookRequest(UpdateBookRequest request) {
        Book record = new Book();
        record.setId(request.getId());
        record.setName(request.getName());
        record.setIsbn(request.getIsbn());
        record.setAuthor(request.getAuthor());
        record.setEdition(request.getEdition());
        record.setPress(request.getPress());
        record.setPublishedAt(request.getPublishedAt());
        record.setRemark(request.getRemark());
        bookMapper.updateByPrimaryKeySelective(record);
    }

}
