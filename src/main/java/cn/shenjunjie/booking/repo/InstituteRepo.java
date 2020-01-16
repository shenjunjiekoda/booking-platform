package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.InstituteMapper;
import cn.shenjunjie.booking.entity.Institute;
import cn.shenjunjie.booking.entity.InstituteExample;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class InstituteRepo {

    @Resource
    private InstituteMapper instituteMapper;

    public List<Institute> selectAll() {
        InstituteExample example = new InstituteExample();
        return instituteMapper.selectByExample(example);
    }

    public void insertByName(String name) {
        Institute record = new Institute();
        record.setName(name);
        instituteMapper.insertSelective(record);
    }


}
