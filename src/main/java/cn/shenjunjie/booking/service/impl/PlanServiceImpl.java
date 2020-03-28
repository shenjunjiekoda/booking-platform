package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.annotation.AuditLog;
import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.*;
import cn.shenjunjie.booking.dto.response.GetPlanBooksResponse;
import cn.shenjunjie.booking.dto.response.GetPlanResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.*;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.enums.OperationType;
import cn.shenjunjie.booking.enums.PlanBookStatus;
import cn.shenjunjie.booking.repo.*;
import cn.shenjunjie.booking.service.PlanService;
import cn.shenjunjie.booking.utils.MessageUtil;
import cn.shenjunjie.booking.utils.UserUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    private MessageUtil messageUtil;

    @Override
    public PageBean<GetPlanResponse> getPlans(GetPlanRequest request) {
        List<GetPlanResponse> planResponseList = Lists.newArrayList();
        Long clazzId = null;
        if (!Strings.isNullOrEmpty(request.getClassName())) {
            Class clazz = classRepo.selectByName(request.getClassName());
            if (clazz != null) {
                clazzId = clazz.getId();
            }
        }
        List<Long> courseIds = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(request.getCourseName())) {
            List<Course> courses = courseRepo.selectByPartName(request.getCourseName());
            if (!CollectionUtils.isEmpty(courses)) {
                courses.stream().map(Course::getId).forEach(courseIds::add);
            }
        }
        List<Long> teacherIds = Lists.newArrayList();
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
        long size = planResponseList.size();
        if(!CollectionUtils.isEmpty(planResponseList)) {
            planResponseList.stream().skip(request.getPageSize() * (request.getPageCurrent() - 1))
                    .limit(request.getPageSize()).collect(Collectors.toList());
        }
        return new PageBean<>(request, planResponseList, size);
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
            courseRepo.insertByName(request.getCourseName());
            course = courseRepo.selectByName(request.getCourseName());
        }
        Long courseId = course.getId();
        Class clazz = classRepo.selectByName(request.getClassName());
        if (clazz == null) {
            return RestBody.fail("班级不存在！");
        }
        if (request.getYear() < 2000 && request.getYear() > 3000) {
            return RestBody.fail("输入的学年不合法！");
        }
        if (StringUtils.isNotBlank(request.getWeek()) && (!request.getWeek().contains("~") || request.getWeek().trim().length() > 5)) {
            return RestBody.fail("输入的周次不合法！");
        }
        Long classId = clazz.getId();
        planRepo.insertByTeacherIdAndCourseIdAndClassIdAndYearAndSemesterAndWeek(teacherId, courseId, classId, request.getYear(), request.getSemester(), request.getWeek());
        Plan plan = planRepo.selectByTeacherIdAndCourseIdAndClassIdAndYearAndSemesterAndWeek(teacherId, courseId, classId, request.getYear(), request.getSemester(), request.getWeek());
        planBookRepo.insertByPlanIdAndStatus(plan.getId(),PlanBookStatus.NEW);
        return RestBody.succeed();
    }

    @Transactional
    @Override
    public RestBody updatePlan(UpdatePlanRequest request) {
        Course course = courseRepo.selectByName(request.getCourseName());
        if (course == null) {
            courseRepo.insertByName(request.getCourseName());
            course = courseRepo.selectByName(request.getCourseName());
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
    public PageBean<GetPlanBooksResponse> getPlanBooks(GetPlanBookRequest request) {
        Long classId = request.getClassId();
        String status = request.getStatus();
        List<GetPlanBooksResponse> planBooksResponseList = Lists.newArrayList();
        List<Plan> plans = planRepo.selectByClassIdAndYearAndSemester(classId,request.getYear(),request.getSemester());
        Class clazz = classRepo.selectById(classId);
        String className = clazz.getName();
        if (!CollectionUtils.isEmpty(plans)) {
            List<GetPlanBooksResponse> finalPlanBooksResponseList = planBooksResponseList;
            plans.forEach(plan -> {
                PlanBook planBook = null;
                if ("all".equals(status)) {
                    planBook = planBookRepo.selectByPlanId(plan.getId());
                } else {
                    planBook = planBookRepo.selectByPlanIdAndStatus(plan.getId(), status);
                }
                if (planBook != null) {
                    GetPlanBooksResponse response = new GetPlanBooksResponse();
                    response.setYear(plan.getYear());
                    response.setSemester(plan.getSemester());
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
                        response.setIsbn(book.getIsbn());
                        response.setEdition(book.getEdition());
                        response.setPress(book.getPress());
                        response.setPublishedAt(book.getPublishedAt());
                        response.setAuthor(book.getAuthor());
                    }
                    response.setCourseId(courseId);
                    response.setCourseName(course.getName());
                    if (teacher != null) {
                        response.setTeacherId(teacherId);
                        response.setTeacherName(teacher.getName());
                    }
                    response.setStuNum(planBook.getStuNum());
                    response.setTeacherNum(planBook.getTeacherNum());
                    response.setActualNum(planBook.getActualNum());
                    response.setClassId(classId);
                    response.setClassName(className);
                    response.setStatus(planBook.getStatus());
                    response.setWeek(week);
                    finalPlanBooksResponseList.add(response);
                }
            });
        }
        int size = planBooksResponseList.size();
        if (!CollectionUtils.isEmpty(planBooksResponseList)) {
            planBooksResponseList = planBooksResponseList.stream().skip(request.getPageSize() * (request.getPageCurrent() - 1))
                    .limit(request.getPageSize()).collect(Collectors.toList());
        }
        return new PageBean<>(request, planBooksResponseList, size);
    }

    @Transactional
    @Override
    public RestBody addPlanBook(AddPlanBookRequest request) {
        request.getBookIds().forEach(bookId -> {
            planBookRepo.insertByPlanIdAndBookIdAndStatusAndStuNumAndTeacherNum(request.getPlanId(), bookId, PlanBookStatus.NEW, request.getStuNum(), request.getTeacherNum());

        });
        return RestBody.succeed();
    }

    @AuditLog(type = OperationType.UPDATE)
    @Transactional
    @Override
    public RestBody updatePlanBook(UpdatePlanBookRequest request) {
        Book book = bookRepo.selectByName(request.getBookName());
        PlanBook planBook = planBookRepo.selectById(request.getPlanBookId());
        planBookRepo.updateByIdAndBookIdAndTeacherNumAndStuNumAndActualNum(request.getPlanBookId(), book.getId(), request.getTeacherNum(), request.getStuNum(), request.getActualNum());
        Long sum = request.getTeacherNum() + request.getStuNum();
        if (sum > 0 && sum.intValue() <= request.getActualNum() && !planBook.getStatus().equals(PlanBookStatus.NEW.getStatus())) {
            planBookRepo.updateByIdAndStatus(request.getPlanBookId(), PlanBookStatus.IN_STOCK);
            messageUtil.sendInStockMessage(planBookRepo.selectById(request.getPlanBookId()));
        } else if(sum > 0 && request.getActualNum() >= 0 && !planBook.getStatus().equals(PlanBookStatus.NEW.getStatus())){
            planBookRepo.updateByIdAndStatus(request.getPlanBookId(),PlanBookStatus.NOT_IN_STOCK);
            messageUtil.sendNotInStockMessage(planBookRepo.selectById(request.getPlanBookId()));
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

    @AuditLog(type = OperationType.SUBMIT)
    @Transactional
    @Override
    public RestBody submitPlanBook(Long id) {
        PlanBook planBook = planBookRepo.selectById(id);
        Plan plan = planRepo.selectById(planBook.getPlanId());
        Teacher teacher = teacherRepo.selectById(plan.getTeacherId());
        if (!UserUtil.getCurrentUserName().equals(teacher.getName()) && !UserUtil.isAdmin()) {
            return RestBody.fail("必须由本人或管理员来提交！");
        }
        if (planBook.getTeacherNum() + planBook.getStuNum() == 0) {
            return RestBody.fail("不需要订书，无需提交");
        }
        if (!planBook.getStatus().equals(PlanBookStatus.NEW.getStatus())) {
            return RestBody.fail("状态异常，请检查最新状态");
        }
        planBookRepo.updateByIdAndStatus(id, PlanBookStatus.SUBMITTED);
        if (allSubmitted(plan.getClassId())) {
            messageUtil.sendAllSubmittedMessageToAdmin(plan.getClassId(), plan);
        }
        return RestBody.succeed(planBook);
    }

    private boolean allSubmitted(Long classId) {
        List<PlanBook> submittedList = planBookRepo.selectByClassIdAndStatus(classId, PlanBookStatus.SUBMITTED);
        List<PlanBook> list = planBookRepo.selectByClassId(classId);
        list = list.stream().filter(planBook -> {
            if (planBook.getStuNum() + planBook.getTeacherNum() == 0) {
                return false;
            }
            if (planBook.getStatus().equals(PlanBookStatus.IN_STOCK.getStatus()) || planBook.getStatus().equals(PlanBookStatus.NOT_IN_STOCK.getStatus())) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        return submittedList.size() == list.size();
    }
}
