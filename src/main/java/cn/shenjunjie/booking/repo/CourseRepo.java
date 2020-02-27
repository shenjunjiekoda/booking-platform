package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.CourseMapper;
import cn.shenjunjie.booking.entity.Course;
import cn.shenjunjie.booking.entity.CourseExample;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class CourseRepo {

    @Resource
    private CourseMapper courseMapper;

    private final static String PERCENT_SIGN = "%";

    public Course selectById(Long id){
        return courseMapper.selectByPrimaryKey(id);
    }

    public Course selectByName(String name){
        return courseMapper.selectByName(name);
    }

    public List<Course> selectByPartName(String name){
        CourseExample example = new CourseExample();
        CourseExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike(PERCENT_SIGN.concat(name).concat(PERCENT_SIGN));
        return courseMapper.selectByExample(example);
    }

    public void insertByName(String name){
        Course record = new Course();
        record.setName(name);
        courseMapper.insertSelective(record);
    }

    public void updateByIdAndName(Long id, String name){
        Course record = new Course();
        record.setId(id);
        record.setName(name);
        courseMapper.updateByPrimaryKeySelective(record);
    }

}
