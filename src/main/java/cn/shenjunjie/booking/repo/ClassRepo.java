package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.ClassMapper;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.entity.ClassExample;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class ClassRepo {

    @Resource
    private ClassMapper classMapper;

    private final static String PERCENT_SIGN = "%";

    public Class selectById(Long id){
        return classMapper.selectByPrimaryKey(id);
    }

    public List<Class> selectByInstituteId(Long instituteId) {
        ClassExample example = new ClassExample();
        ClassExample.Criteria criteria = example.createCriteria();
        criteria.andInstituteIdEqualTo(instituteId);
        return classMapper.selectByExample(example);
    }

    public Class selectByName(String name) {
        return classMapper.selectByName(name);
    }

    public List<Class> selectByPartName(String name){
        ClassExample example = new ClassExample();
        ClassExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike(PERCENT_SIGN.concat(name).concat(PERCENT_SIGN));
        return classMapper.selectByExample(example);
    }

    public void insertByInstituteIdAndName(Long instituteId, String name) {
        Class record = new Class();
        record.setName(name);
        record.setInstituteId(instituteId);
        classMapper.insertSelective(record);
    }

    public void updateByIdAndName(Long id, String name) {
        Class record = new Class();
        record.setId(id);
        record.setName(name);
        classMapper.updateByPrimaryKeySelective(record);
    }

    public void deleteById(Long id) {
        classMapper.deleteByPrimaryKey(id);
    }

}
