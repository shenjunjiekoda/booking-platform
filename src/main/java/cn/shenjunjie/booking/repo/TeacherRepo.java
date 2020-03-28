package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.TeacherMapper;
import cn.shenjunjie.booking.dto.request.RegisterRequest;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.entity.TeacherExample;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class TeacherRepo {

    @Resource
    private TeacherMapper teacherMapper;

    private final static String PERCENT_SIGN = "%";

    public Teacher selectByTeacherId(String teacherId) {
        return teacherMapper.selectByTeacherId(teacherId);
    }

    public Teacher selectById(Long id){
        return teacherMapper.selectByPrimaryKey(id);
    }

    public Teacher selectByEmail(String email) {
        return teacherMapper.selectByEmail(email);
    }

    public Teacher selectByName(String name){
        return teacherMapper.selectByName(name);
    }

    public List<Teacher> selectByPartName(String name){
        TeacherExample example = new TeacherExample();
        TeacherExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike(PERCENT_SIGN.concat(name).concat(PERCENT_SIGN));
        return teacherMapper.selectByExample(example);
    }

    public List<Teacher> selectAdmins(){
        TeacherExample example = new TeacherExample();
        TeacherExample.Criteria criteria = example.createCriteria();
        criteria.andIsadminEqualTo(1);
        return teacherMapper.selectByExample(example);
    }

    public void insertByRequest(RegisterRequest request){
        Teacher record = new Teacher();
        record.setEmail(request.getEmail());
        record.setName(request.getName());
        record.setIsadmin(request.getIsAdmin());
        record.setPassword(request.getPassword());
        record.setTeacherId(request.getTeacherId());
        teacherMapper.insertSelective(record);
    }

}
