package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.*;
import cn.shenjunjie.booking.dto.response.GetPlanBooksResponse;
import cn.shenjunjie.booking.dto.response.GetPlanResponse;
import cn.shenjunjie.booking.entity.*;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.enums.BookStatus;
import cn.shenjunjie.booking.repo.*;
import cn.shenjunjie.booking.service.PlanBookService;
import cn.shenjunjie.booking.service.PlanService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class PlanServiceImpl implements PlanService {

    @Resource
    private PlanRepo planRepo;
    @Resource
    private PlanBookRepo planBookRepo;
    @Resource
    private ClassRepo classRepo;
    @Resource
    private TeacherRepo teacherRepo;
    @Resource
    private CourseRepo courseRepo;
    @Resource
    private BookRepo bookRepo;
    @Resource
    private PlanBookService planBookService;

    @Override
    public List<GetPlanResponse> getPlans(GetPlanRequest request) {
        List<GetPlanResponse> planResponseList = Lists.newArrayList();
        Long clazzId = null;
        if (!Strings.isNullOrEmpty(request.getClassName())) {
            Class clazz = classRepo.selectByName(request.getClassName());
            if (clazz != null) {
                clazzId = clazz.getId();
            }
        }
        List<Long> courseIds = null;
        if (!Strings.isNullOrEmpty(request.getCourseName())) {
            List<Course> courses = courseRepo.selectByPartName(request.getCourseName());
            if (!CollectionUtils.isEmpty(courses)) {
                courses.stream().map(Course::getId).forEach(courseIds::add);
            }
        }
        List<Long> teacherIds = null;
        if (!Strings.isNullOrEmpty(request.getTeacherName())) {
            List<Teacher> teachers = teacherRepo.selectByPartName(request.getTeacherName());
            if (!CollectionUtils.isEmpty(teachers)) {
                teachers.stream().map(Teacher::getId).forEach(teacherIds::add);
            }
        }
        String week = null;
        if (request.getWeekStart() != null && request.getWeekEnd() != null) {
            week = request.getWeekStart() + "~" + request.getWeekEnd();
        }
        List<Plan> plans = planRepo.selectByClassIdAndTeacherIdsAndCourseIdsAndWeekAndYearAndSemester(clazzId, teacherIds, courseIds, week, request.getYear(), request.getSemester());
        if (!CollectionUtils.isEmpty(plans)) {
            plans.forEach(item -> {
                GetPlanResponse response = new GetPlanResponse();
                Class clazz = classRepo.selectById(item.getClassId());
                Course course = courseRepo.selectById(item.getCourseId());
                String teacherName = null;
                if (item.getTeacherId() != null) {
                    Teacher teacher = teacherRepo.selectById(item.getTeacherId());
                    teacherName = teacher.getName();
                }
                response.setClassName(clazz.getName())
                        .setCourseName(course.getName())
                        .setTeacherName(teacherName);
                BeanUtils.copyProperties(item, response);
                planResponseList.add(response);
            });
        }
        return planResponseList;
    }

    @Transactional
    @Override
    public RestBody addPlan(AddPlanRequest request) {
        Teacher teacher = teacherRepo.selectByName(request.getTeacherName());
        if (teacher == null) {
            return RestBody.fail("老师不存在！");
        }
        Long teacherId = teacher.getId();
        Course course = courseRepo.selectByName(request.getCourseName());
        if (course == null) {
            return RestBody.fail("课程不存在！");
        }
        Long courseId = course.getId();
        Class clazz = classRepo.selectByName(request.getClassName());
        if (clazz == null) {
            return RestBody.fail("班级不存在！");
        }
        Long classId = clazz.getId();
        planRepo.insertByTeacherIdAndCourseIdAndClassIdAndWeek(teacherId, courseId, classId, request.getWeek());
        return RestBody.succeed();
    }

    @Transactional
    @Override
    public RestBody updatePlan(UpdatePlanRequest request) {
        Course course = courseRepo.selectByName(request.getCourseName());
        if (course == null) {
            return RestBody.fail("课程不存在！");
        }
        Long courseId = course.getId();
        Class clazz = classRepo.selectByName(request.getClassName());
        if (clazz == null) {
            return RestBody.fail("班级不存在！");
        }
        Long classId = clazz.getId();
        Teacher teacher = teacherRepo.selectByName(request.getTeacherName());
        if (teacher == null) {
            return RestBody.fail("老师名不存在！");
        }
        Long teacherId = teacher.getId();
        planRepo.updateByIdAndTeacherIdAndCourseIdAndClassIdAndWeek(request.getId(), teacherId, courseId, classId, request.getWeek());
        return RestBody.succeed();
    }

    @Override
    public List<GetPlanBooksResponse> getPlanBooks(Long classId) {
        List<GetPlanBooksResponse> planBooksResponseList = Lists.newArrayList();
        List<Plan> plans = planRepo.selectByClassId(classId);
        Class clazz = classRepo.selectById(classId);
        String className = clazz.getName();
        if (!CollectionUtils.isEmpty(plans)) {
            plans.forEach(plan -> {
                PlanBook planBook = planBookRepo.selectByPlanId(plan.getId());
                if (planBook != null) {
                    GetPlanBooksResponse response = new GetPlanBooksResponse();
                    Long teacherId = plan.getTeacherId();
                    Teacher teacher = teacherRepo.selectById(teacherId);
                    Long courseId = plan.getCourseId();
                    Course course = courseRepo.selectById(courseId);
                    String week = plan.getWeek();
                    response.setId(planBook.getId());
                    if (planBook.getBookId() != null) {
                        response.setBookId(planBook.getBookId());
                        Book book = bookRepo.selectById(planBook.getBookId());
                        response.setBookName(book.getName());
                    }
                    response.setCourseId(courseId);
                    response.setCourseName(course.getName());
                    if (teacher != null) {
                        response.setTeacherId(teacherId);
                        response.setTeacherName(teacher.getName());
                    }
                    response.setStuNum(planBook.getStuNum());
                    response.setTeacherNum(planBook.getTeacherNum());
                    response.setClassId(classId);
                    response.setClassName(className);
                    response.setStatus(planBook.getStatus());
                    response.setWeek(week);
                    planBooksResponseList.add(response);
                }
            });
        }
        return planBooksResponseList;
    }

    @Transactional
    @Override
    public RestBody addPlanBook(AddPlanBookRequest request) {
//        Teacher teacher = teacherRepo.selectByName(request.getTeacherName());
//        if(teacher == null){
//            return RestBody.fail("老师不存在！");
//        }
//        Long teacherId = teacher.getId();
//        Course course = courseRepo.selectByName(request.getCourseName());
//        if(course == null){
//            return RestBody.fail("课程不存在！");
//        }
//        Long courseId = course.getId();
//        Class clazz = classRepo.selectByName(request.getClassName());
//        if(clazz == null){
//            return RestBody.fail("班级不存在！");
//        }
//        Long classId = clazz.getId();
//        planRepo.insertByTeacherIdAndCourseIdAndClassIdAndWeek(teacherId,courseId,classId,request.getWeek());

//        Plan plan = planRepo.selectByClassIdAndTeacherIdAndCourseId(classId, teacherId, courseId);
        request.getBookIds().forEach(bookId -> {
            planBookRepo.insertByPlanIdAndBookIdAndStatusAndStuNumAndTeacherNum(request.getPlanId(), bookId, BookStatus.NEW, request.getStuNum(), request.getTeacherNum());

        });
        return RestBody.succeed();
    }

    @Transactional
    @Override
    public RestBody updatePlanBook(UpdatePlanBookRequest request) {
        Teacher teacher = null;
        if (request.getTeacherName() != null) {
            teacher = teacherRepo.selectByName(request.getTeacherName());
            if (teacher == null) {
                return RestBody.fail("老师不存在！");
            }
        }
        Long teacherId = teacher.getId();
        Course course = null;
        if (request.getCourseName() != null) {
            course = courseRepo.selectByName(request.getCourseName());
            if (course == null) {
                return RestBody.fail("课程不存在！");
            }
        }
        Long courseId = course.getId();
        Class clazz = null;
        if (request.getClassName() != null) {
            clazz = classRepo.selectByName(request.getClassName());
            if (clazz == null) {
                return RestBody.fail("班级不存在！");
            }
        }
        Long classId = clazz.getId();
        planRepo.updateByIdAndTeacherIdAndCourseIdAndClassIdAndWeek(request.getPlanId(), teacherId, courseId, classId, request.getWeek());
        if (request.getTeacherNum() != null || request.getStuNum() != null || request.getBookId() != null) {
            PlanBook planBook = planBookRepo.selectByPlanId(request.getPlanId());
            planBookRepo.updateByIdAndBookIdAndTeacherNumAndStuNum(planBook.getId(), request.getBookId(), request.getTeacherNum(), request.getStuNum());
        }

        return RestBody.succeed();
    }

    @Transactional
    @Override
    public RestBody deletePlanBook(Long id) {
        PlanBook planBook = planBookRepo.selectById(id);
        if (planBook == null) {
            return RestBody.fail("计划号不存在！");
        }
        planRepo.deleteById(planBook.getPlanId());
        planBookRepo.deleteById(id);
        return RestBody.succeed();
    }
}
