package cn.shenjunjie.booking.dao;

import cn.shenjunjie.booking.entity.TeacherClass;
import cn.shenjunjie.booking.entity.TeacherClassExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TeacherClassMapper {
    long countByExample(TeacherClassExample example);

    int deleteByExample(TeacherClassExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TeacherClass record);

    int insertSelective(TeacherClass record);

    List<TeacherClass> selectByExample(TeacherClassExample example);

    TeacherClass selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TeacherClass record, @Param("example") TeacherClassExample example);

    int updateByExample(@Param("record") TeacherClass record, @Param("example") TeacherClassExample example);

    int updateByPrimaryKeySelective(TeacherClass record);

    int updateByPrimaryKey(TeacherClass record);
}