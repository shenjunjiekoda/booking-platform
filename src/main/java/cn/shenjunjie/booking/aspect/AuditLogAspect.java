package cn.shenjunjie.booking.aspect;

import cn.shenjunjie.booking.annotation.AuditLog;
import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddPlanRequest;
import cn.shenjunjie.booking.dto.request.UpdatePlanBookRequest;
import cn.shenjunjie.booking.dto.request.UpdatePlanRequest;
import cn.shenjunjie.booking.entity.*;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.enums.OperationType;
import cn.shenjunjie.booking.repo.*;
import cn.shenjunjie.booking.service.OperationLogService;
import cn.shenjunjie.booking.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/19 15:04
 */
@Slf4j
@Component
@Aspect
public class AuditLogAspect {

    @Resource
    private OperationLogService service;
    @Resource
    private ClassRepo classRepo;
    @Resource
    private CourseRepo courseRepo;
    @Resource
    private PlanBookRepo planBookRepo;
    @Resource
    private BookRepo bookRepo;
    @Resource
    private PlanRepo planRepo;


    @Around("@annotation(auditlog)")
    public Object operationLog(ProceedingJoinPoint point, AuditLog auditlog) throws Throwable {
        log.debug("do operationLog method:{}", point.getSignature().getName());
        Object result = point.proceed();
        insertToDB(point.getArgs(), auditlog.type(), point.getSignature().getName(), result);
        return result;
    }

    private void insertToDB(Object[] args, OperationType type, String methodName, Object result) {
        switch (methodName) {
            case "addPlan":
                resolveAddPlan(args, type, (RestBody) result);
                break;
            case "updatePlan":
                resolveUpdatePlan(args, type, (RestBody) result);
                break;
            case "updatePlanBook":
                resolveUpdatePlanBook(args, type, (RestBody) result);
                break;
            case "submitPlanBook":
                resolveSubmitPlanBook(args, type, (RestBody) result);
                break;
            default:
                break;
        }
    }

    public void resolveAddPlan(Object[] args, OperationType type, RestBody result) {
        if (result.getErrorCode() != 0) {
            return;
        }
        AddPlanRequest request = (AddPlanRequest) args[0];
        String msg = "班级" + request.getClassName() + "添加了" + request.getTeacherName() + "老师的上课计划, 课程名：" + request.getCourseName();
        Class clazz = classRepo.selectByName(request.getClassName());
        service.insert(clazz.getId(), type, msg);
    }

    public void resolveUpdatePlan(Object[] args, OperationType type, RestBody result) {
        if (result.getErrorCode() != 0) {
            return;
        }
        UpdatePlanRequest request = (UpdatePlanRequest) args[0];
        String msg = "在班级" + request.getClassName() + "更新了" + request.getTeacherName() + "老师的上课计划, 课程名：" + request.getCourseName();
        Class clazz = classRepo.selectByName(request.getClassName());
        service.insert(clazz.getId(), type, msg);
    }

    public void resolveSubmitPlanBook(Object[] args, OperationType type, RestBody result) {
        if (result.getErrorCode() != 0) {
            return;
        }
        Long planBookId = (Long) args[0];
        PlanBook planBook = planBookRepo.selectById(planBookId);
        Book book = bookRepo.selectById(planBook.getBookId());
        Plan plan = planRepo.selectById(planBook.getPlanId());
        Class clazz = classRepo.selectById(plan.getClassId());
        Course course = courseRepo.selectById(plan.getCourseId());
        String msg = UserUtil.getCurrentUserName() + "老师为" + plan.getYear() + "学年第" + plan.getSemester() + "学期" +
                clazz.getName() + "班级的" + course.getName() + "课程提交了订书信息: 书名:" + book.getName()
                + " 老师需订购数量:" + planBook.getTeacherNum() + "本，学生需订购数量：" + planBook.getStuNum() + "本";
        service.insert(clazz.getId(), type, msg);
    }

    public void resolveUpdatePlanBook(Object[] args, OperationType type, RestBody result) {
        if (result.getErrorCode() != 0) {
            return;
        }
        UpdatePlanBookRequest request = (UpdatePlanBookRequest) args[0];
        PlanBook planBook = planBookRepo.selectById(request.getPlanBookId());
        Plan plan = planRepo.selectById(planBook.getPlanId());
        Course course = courseRepo.selectById(plan.getCourseId());
        Class clazz = classRepo.selectById(plan.getClassId());
        String msg = "";
        switch (planBook.getStatus()) {
            case "new":
                msg = UserUtil.getCurrentUserName() + "更新了" + plan.getYear() + "学年第" + plan.getSemester() +
                        "学期,班级" + clazz.getName() + ",课程：" + course.getName() + "的订单信息: 书名:" + request.getBookName() + " ，老师订书数量："
                        + request.getTeacherNum() + "本，学生订书数量：" + request.getStuNum() + "本";
                break;
            case "instock":
                msg = "有新的书本已到货。" + UserUtil.getCurrentUserName() + "更新了" + plan.getYear() + "学年第" + plan.getSemester() +
                        "学期,班级" + clazz.getName() + ",课程：" + course.getName() + "的订单信息: 书名:" + request.getBookName() + " ，老师订书数量："
                        + request.getTeacherNum() + "本，学生订书数量：" + request.getStuNum() + "本," + "实到数量：" + request.getActualNum() + "本";

                break;
            case "notinstock":
                msg = "有新的到货书本数量已经更新，书本未全部成功到货。" + UserUtil.getCurrentUserName() + "更新了" + plan.getYear() + "学年第" + plan.getSemester() +
                        "学期,班级" + clazz.getName() + ",课程：" + course.getName() + "的订单信息: 书名:" + request.getBookName() + " ，老师订书数量："
                        + request.getTeacherNum() + "本，学生订书数量：" + request.getStuNum() + "本," + "实到数量：" + request.getActualNum() + "本";
                break;
        }
        service.insert(clazz.getId(), type, msg);
    }


}
