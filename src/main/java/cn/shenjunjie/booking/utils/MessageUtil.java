package cn.shenjunjie.booking.utils;

import cn.shenjunjie.booking.entity.*;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.repo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/26 17:40
 */
@Component
public class MessageUtil {

    @Resource
    private TeacherRepo teacherRepo;
    @Resource
    private MessageRepo messageRepo;
    @Resource
    private ClassRepo classRepo;
    @Resource
    private PlanRepo planRepo;
    @Resource
    private BookRepo bookRepo;


    public void sendAllSubmittedMessageToAdmin(Long classId, Plan plan) {
        Class clazz = classRepo.selectById(classId);
        String data = "班级" + clazz.getName() + "的" + plan.getYear() + "~" + (plan.getYear() + 1) + "学年第" + plan.getSemester()
                + "学期所有订书请求已提交，请前往查看!";
        sendMessageToAdmin(data);
    }

    public void sendMessageToAdmin(String data) {
        List<Teacher> admins = teacherRepo.selectAdmins();
        if (CollectionUtils.isNotEmpty(admins)) {
            admins.stream().map(Teacher::getId).forEach(to -> {
                insertMessage(to, data);
            });
        }
    }

    public void sendInStockMessage(PlanBook planBook) {
        Plan plan = planRepo.selectById(planBook.getPlanId());
        Class clazz = classRepo.selectById(plan.getClassId());
        Book book = bookRepo.selectById(planBook.getBookId());
        String data = "你为班级" + clazz.getName() + "订的" + book.getName() + "一书已全部到货，实到数量：" + planBook.getActualNum()
                + "本。请尽早联系教材科领取书。";
        sendMessageToUser(plan.getTeacherId(), data);
    }

    public void sendNotInStockMessage(PlanBook planBook) {
        Plan plan = planRepo.selectById(planBook.getPlanId());
        Class clazz = classRepo.selectById(plan.getClassId());
        Book book = bookRepo.selectById(planBook.getBookId());
        String data = "你为班级" + clazz.getName() + "订的" + book.getName() + "一书到货数量不足，实到数量：" + planBook.getActualNum()
                + "本。请联系教材科询问详情";
        sendMessageToUser(plan.getTeacherId(), data);
    }

    public void sendMessage(Long from, Long to, String data) {
        insertMessage(from, to, data);
    }

    public void sendMessageToUser(Long to, String data) {
        insertMessage(UserUtil.getCurrentUser().getId(), to, data);
    }

    public void insertMessage(Long to, String data) {
        insertMessage(UserUtil.getCurrentUser().getId(), to, data);
    }

    public void insertMessage(Long from, Long to, String data) {
        Message record = new Message();
        record.setData(data);
        record.setFrom(from);
        record.setTo(to);
        record.setReaded(0);
        messageRepo.insertByRecord(record);
    }

}
