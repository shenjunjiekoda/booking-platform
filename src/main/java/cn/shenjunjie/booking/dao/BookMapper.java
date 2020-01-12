package cn.shenjunjie.booking.dao;

import cn.shenjunjie.booking.entity.Book;
import cn.shenjunjie.booking.entity.BookExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author JunjieShen
 */
public interface BookMapper {
    long countByExample(BookExample example);

    int deleteByExample(BookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Book record);

    int insertSelective(Book record);

    List<Book> selectByExample(BookExample example);

    Book selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Book record, @Param("example") BookExample example);

    int updateByExample(@Param("record") Book record, @Param("example") BookExample example);

    int updateByPrimaryKeySelective(Book record);

    int updateByPrimaryKey(Book record);
}