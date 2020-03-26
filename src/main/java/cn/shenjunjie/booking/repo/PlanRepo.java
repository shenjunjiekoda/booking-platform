package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.PlanMapper;
import cn.shenjunjie.booking.entity.Plan;
import cn.shenjunjie.booking.entity.PlanExample;
import com.google.common.base.Strings;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class PlanRepo {

    @Resource
    private PlanMapper planMapper;

    public Plan selectById(Long id) {
        return planMapper.selectByPrimaryKey(id);
    }

    public List<Plan> selectByClassIdAndTeacherIdsAndCourseIdsAndWeekAndYearAndSemester(Long classId, List<Long> teacherIds, List<Long> courseIds, String week, Integer year, Integer semester) {
        PlanExample example = new PlanExample();
        PlanExample.Criteria criteria = example.createCriteria();
        if (classId != null) {
            criteria.andClassIdEqualTo(classId);
        }
        if (!CollectionUtils.isEmpty(teacherIds)) {
            criteria.andTeacherIdIn(teacherIds);
        }
        if (!CollectionUtils.isEmpty(courseIds)) {
            criteria.andCourseIdIn(courseIds);
        }
        if (!Strings.isNullOrEmpty(week)) {
            criteria.andWeekEqualTo(week);
        }
        if (year != null) {
            criteria.andYearEqualTo(year);
        }
        if (semester != null) {
            criteria.andSemesterEqualTo(semester);
        }
        return planMapper.selectByExample(example);
    }

    public List<Plan> selectByClassIdAndYearAndSemester(Long classId,Integer year,Integer semester) {
        PlanExample example = new PlanExample();
        PlanExample.Criteria criteria = example.createCriteria();
        criteria.andClassIdEqualTo(classId);
        criteria.andYearEqualTo(year);
        criteria.andSemesterEqualTo(semester);
        return planMapper.selectByExample(example);
    }

//    public Plan selectByClassIdAndTeacherIdAndCourseId(Long classId,Long teacherId,Long courseId){
//        return planMapper.selectByClassIdAndTeacherIdAndCourseId(classId,teacherId,courseId);
//    }

    public void insertByTeacherIdAndCourseIdAndClassIdAndYearAndSemesterAndWeek(Long teacherId, Long courseId, Long classId, Integer year, Integer semester, String week) {
        Plan record = new Plan();
        record.setTeacherId(teacherId);
        record.setClassId(classId);
        record.setCourseId(courseId);
        record.setWeek(week);
        record.setYear(year);
        record.setSemester(semester);
        planMapper.insertSelective(record);
    }

    public void updateByIdAndTeacherIdAndCourseIdAndClassIdAndWeek(Long id, Long teacherId, Long courseId, Long classId, String week) {
        Plan record = new Plan();
        record.setId(id);
        record.setTeacherId(teacherId);
        record.setClassId(classId);
        record.setCourseId(courseId);
        record.setWeek(week);
        planMapper.updateByPrimaryKeySelective(record);
    }

    public void deleteById(Long id) {
        planMapper.deleteByPrimaryKey(id);
    }

    public Plan selectByTeacherIdAndCourseIdAndClassIdAndYearAndSemesterAndWeek(Long teacherId, Long courseId, Long classId, Integer year, Integer semester, String week) {
        PlanExample example = new PlanExample();
        PlanExample.Criteria criteria = example.createCriteria();
        criteria.andTeacherIdEqualTo(teacherId);
        criteria.andCourseIdEqualTo(courseId);
        criteria.andClassIdEqualTo(classId);
        criteria.andYearEqualTo(year);
        criteria.andSemesterEqualTo(semester);
        criteria.andWeekEqualTo(week);
        List<Plan> plans = planMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(plans) && plans.size() == 1){
            return plans.get(0);
        }
        return null;
    }
}
