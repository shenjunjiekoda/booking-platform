package cn.shenjunjie.booking.dao;

import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.entity.ClassExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author JunjieShen
 */
public interface ClassMapper {
    long countByExample(ClassExample example);

    int deleteByExample(ClassExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Class record);

    int insertSelective(Class record);

    List<Class> selectByExample(ClassExample example);

    Class selectByPrimaryKey(Long id);

    Class selectByName(@Param("name") String name);

    int updateByExampleSelective(@Param("record") Class record, @Param("example") ClassExample example);

    int updateByExample(@Param("record") Class record, @Param("example") ClassExample example);

    int updateByPrimaryKeySelective(Class record);

    int updateByPrimaryKey(Class record);
}