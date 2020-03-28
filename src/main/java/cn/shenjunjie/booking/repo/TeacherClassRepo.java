package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.TeacherClassMapper;
import cn.shenjunjie.booking.entity.ClassExample;
import cn.shenjunjie.booking.entity.OperationLog;
import cn.shenjunjie.booking.entity.TeacherClass;
import cn.shenjunjie.booking.entity.TeacherClassExample;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class TeacherClassRepo {

    @Resource
    private TeacherClassMapper teacherClassMapper;

    private final static String PERCENT_SIGN = "%";

    public TeacherClass selectById(Long id){
        return teacherClassMapper.selectByPrimaryKey(id);
    }

    public List<TeacherClass> selectByTeacherId(Long teacherId) {
        TeacherClassExample example = new TeacherClassExample();
        TeacherClassExample.Criteria criteria = example.createCriteria();
        criteria.andTeacherIdEqualTo(teacherId);
        return teacherClassMapper.selectByExample(example);
    }
//
//    public Class selectByName(String name) {
//        return operationLogMapper.selectByName(name);
//    }
//
//    public List<Class> selectByPartName(String name){
//        ClassExample example = new ClassExample();
//        ClassExample.Criteria criteria = example.createCriteria();
//        criteria.andNameLike(PERCENT_SIGN.concat(name).concat(PERCENT_SIGN));
//        return operationLogMapper.selectByExample(example);
//    }
//
    public void insertByTeacherIdAndClassId(Long teacherId, Long classId) {
        TeacherClass record = new TeacherClass();
        record.setTeacherId(teacherId);
        record.setClassId(classId);
        teacherClassMapper.insertSelective(record);
    }

    public void deleteById(Long id) {
        teacherClassMapper.deleteByPrimaryKey(id);
    }

    public void deleteByTeacherId(Long teacherId) {
        TeacherClassExample example = new TeacherClassExample();
        TeacherClassExample.Criteria criteria = example.createCriteria();
        criteria.andTeacherIdEqualTo(teacherId);
        teacherClassMapper.deleteByExample(example);
    }

}
