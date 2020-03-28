package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.OperationLogMapper;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.entity.ClassExample;
import cn.shenjunjie.booking.entity.OperationLog;
import cn.shenjunjie.booking.entity.OperationLogExample;
import cn.shenjunjie.booking.enums.OperationType;
import cn.shenjunjie.booking.utils.UserUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class OperationLogRepo {

    @Resource
    private OperationLogMapper operationLogMapper;

    private final static String PERCENT_SIGN = "%";

    public OperationLog selectById(Long id){
        return operationLogMapper.selectByPrimaryKey(id);
    }

    public List<OperationLog> selectByClassId(Long classId) {
        OperationLogExample example = new OperationLogExample();
        OperationLogExample.Criteria criteria = example.createCriteria();
        criteria.andClassIdEqualTo(classId);
        return operationLogMapper.selectByExample(example);
    }
//
    public List<OperationLog> selectByClassIdAndCreatedAtFromAndCreatedAtTo(Long classId, Date from,Date to) {
        OperationLogExample example = new OperationLogExample();
        OperationLogExample.Criteria criteria = example.createCriteria();
        criteria.andClassIdEqualTo(classId);
        criteria.andCreatedAtBetween(from,to);
        return operationLogMapper.selectByExample(example);
    }

//    public List<Class> selectByPartName(String name){
//        ClassExample example = new ClassExample();
//        ClassExample.Criteria criteria = example.createCriteria();
//        criteria.andNameLike(PERCENT_SIGN.concat(name).concat(PERCENT_SIGN));
//        return operationLogMapper.selectByExample(example);
//    }
//
    public void insertByClassIdAndTypeAndMsg(Long classId, OperationType type, String msg) {
        OperationLog record = new OperationLog();
        record.setClassId(classId);
        record.setType(type.getType());
        record.setCreatedBy(UserUtil.getCurrentUserName());
        record.setMsg(msg);
        operationLogMapper.insertSelective(record);
    }
//
//    public void updateByIdAndName(Long id, String name) {
//        Class record = new Class();
//        record.setId(id);
//        record.setName(name);
//        operationLogMapper.updateByPrimaryKeySelective(record);
//    }

    public void deleteById(Long id) {
        operationLogMapper.deleteByPrimaryKey(id);
    }

}
