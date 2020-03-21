package cn.shenjunjie.booking.dao;

import java.util.List;

import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.entity.TeacherExample;
import org.apache.ibatis.annotations.Param;

/**
 * @author JunjieShen
 */
public interface TeacherMapper {
    long countByExample(TeacherExample example);

    int deleteByExample(TeacherExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    List<Teacher> selectByExample(TeacherExample example);

    Teacher selectByPrimaryKey(Long id);

    Teacher selectByName(@Param("name") String name);

    Teacher selectByTeacherId(@Param("teacherId") String teacherId);

    int updateByExampleSelective(@Param("record") Teacher record, @Param("example") TeacherExample example);

    int updateByExample(@Param("record") Teacher record, @Param("example") TeacherExample example);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);
}