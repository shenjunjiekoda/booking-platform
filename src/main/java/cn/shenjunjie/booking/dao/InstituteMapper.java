package cn.shenjunjie.booking.dao;

import cn.shenjunjie.booking.entity.Institute;
import cn.shenjunjie.booking.entity.InstituteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InstituteMapper {
    long countByExample(InstituteExample example);

    int deleteByExample(InstituteExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Institute record);

    int insertSelective(Institute record);

    List<Institute> selectByExample(InstituteExample example);

    Institute selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Institute record, @Param("example") InstituteExample example);

    int updateByExample(@Param("record") Institute record, @Param("example") InstituteExample example);

    int updateByPrimaryKeySelective(Institute record);

    int updateByPrimaryKey(Institute record);
}