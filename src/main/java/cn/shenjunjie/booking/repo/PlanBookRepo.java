package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.PlanBookMapper;
import cn.shenjunjie.booking.entity.PlanBook;
import cn.shenjunjie.booking.entity.PlanBookExample;
import cn.shenjunjie.booking.enums.PlanBookStatus;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class PlanBookRepo {

    @Resource
    private PlanBookMapper planBookMapper;

    public PlanBook selectById(Long id) {
        return planBookMapper.selectByPrimaryKey(id);
    }

    public List<PlanBook> selectByPlanId(Long planId) {
        PlanBookExample example = new PlanBookExample();
        PlanBookExample.Criteria criteria = example.createCriteria();
        criteria.andPlanIdEqualTo(planId);
        return planBookMapper.selectByExample(example);
    }

    public List<PlanBook> selectByPlanIdAndStatus(Long planId, String status) {
        PlanBookExample example = new PlanBookExample();
        PlanBookExample.Criteria criteria = example.createCriteria();
        criteria.andPlanIdEqualTo(planId);
        criteria.andStatusEqualTo(status);
        return planBookMapper.selectByExample(example);
    }

    public List<PlanBook> selectByClassIdAndStatus(Long classId, PlanBookStatus status) {
        return planBookMapper.selectByClassIdAndStatus(classId,status.getStatus());
    }

    public List<PlanBook> selectByClassId(Long classId) {
        return planBookMapper.selectByClassId(classId);
    }

    public void insertByPlanIdAndBookIdAndStatusAndStuNumAndTeacherNum(Long planId, Long bookId, PlanBookStatus status, Long stuNum, Long teacherNum) {
        PlanBook record = new PlanBook();
        record.setPlanId(planId);
        record.setBookId(bookId);
        record.setStatus(status.getStatus());
        record.setStuNum(stuNum);
        record.setTeacherNum(teacherNum);
        planBookMapper.insertSelective(record);
    }

    public void insertByPlanIdAndStatus(Long planId,PlanBookStatus status) {
        PlanBook record = new PlanBook();
        record.setPlanId(planId);
        record.setStatus(status.getStatus());
        planBookMapper.insertSelective(record);
    }

    public void updateByIdAndBookIdAndTeacherNumAndStuNum(Long id, Long bookId, Long teacherNum, Long stuNum) {
        PlanBook record = new PlanBook();
        record.setId(id);
        record.setBookId(bookId);
        record.setTeacherNum(teacherNum);
        record.setStuNum(stuNum);
        planBookMapper.updateByPrimaryKeySelective(record);
    }

    public void updateByIdAndStatus(Long id, PlanBookStatus status) {
        PlanBook record = new PlanBook();
        record.setId(id);
        record.setStatus(status.getStatus());
        planBookMapper.updateByPrimaryKeySelective(record);
    }

    public void updateByIdAndBookIdAndTeacherNumAndStuNumAndActualNum(Long id, Long bookId,Long teacherNum,Long stuNum,Integer actualNum) {
        PlanBook record = new PlanBook();
        record.setId(id);
        record.setBookId(bookId);
        record.setTeacherNum(teacherNum);
        record.setStuNum(stuNum);
        record.setActualNum(actualNum);
        planBookMapper.updateByPrimaryKeySelective(record);
    }

    public void deleteById(Long id) {
        planBookMapper.deleteByPrimaryKey(id);
    }

}
