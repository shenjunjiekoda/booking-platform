package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.PlanBookMapper;
import cn.shenjunjie.booking.entity.PlanBook;
import cn.shenjunjie.booking.entity.PlanBookExample;
import cn.shenjunjie.booking.enums.BookStatus;
import com.sun.org.apache.bcel.internal.generic.RETURN;
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

    public PlanBook selectByPlanId(Long planId) {
        return planBookMapper.selectByPlanId(planId);
    }

    public void insertByPlanIdAndBookIdAndStatusAndStuNumAndTeacherNum(Long planId, Long bookId, BookStatus status, Long stuNum, Long teacherNum) {
        PlanBook record = new PlanBook();
        record.setPlanId(planId);
        record.setBookId(bookId);
        record.setStatus(status.getStatus());
        record.setStuNum(stuNum);
        record.setTeacherNum(teacherNum);
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

    public void deleteById(Long id) {
        planBookMapper.deleteByPrimaryKey(id);
    }

}
