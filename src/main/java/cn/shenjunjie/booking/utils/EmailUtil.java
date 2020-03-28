package cn.shenjunjie.booking.utils;

import cn.shenjunjie.booking.entity.Message;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.repo.PlanRepo;
import cn.shenjunjie.booking.repo.TeacherRepo;
import com.google.common.collect.Lists;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * @author JunjieShen
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "spring.mail.enabled",havingValue = "true",matchIfMissing = false)
public class EmailUtil {

    @Resource
    private TeacherRepo teacherRepo;
    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 日期显示格式
     */
    private static final String AT = "@";
    private static final String SUBJECT = "来自教材申购领取平台的消息";


    @Async
    public void sendMail(Message message) {
        log.debug("message:{}",message);
        Teacher from = teacherRepo.selectById(message.getFrom());
        Teacher to = teacherRepo.selectById(message.getTo());
        doSend(from, to, message.getData());
    }

    public void doSend(Teacher from, Teacher to, String data) {
        log.debug("prepare to send!");
        String[] rationalRecipients = getRationalRecipients(to);
        if (rationalRecipients != null && rationalRecipients.length > 0) {
            Map<String, Object> model = getModel(from, to, data);
            String content = getContent("mail/message.ftl", model);
            log.debug("content:{}", content);
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, false);
                mailMessage.setSubject(SUBJECT);
                mailMessage.setFrom(sender);
                mailMessage.setTo(rationalRecipients);
                mailMessage.setText(content, true);
                javaMailSender.send(mimeMessage);
                log.info("send success to {}", rationalRecipients.toString());
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private Map<String, Object> getModel(Teacher from, Teacher to, String data) {
        Map<String, Object> model = new HashMap();
        String sendFrom = from.getName() == null ? "未知" : from.getName();
        model.put("from", sendFrom);
        String sendTo = to.getName() == null ? "未知" : to.getName();
        model.put("to", sendTo);
        data = data == null ? "" : data;
        model.put("data", data);
        return model;
    }

    private String getContent(String contentName, Map<String, Object> model) {
        try {
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(contentName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception ex) {
            log.warn("bindBodyError", ex);
        }
        return "";
    }

    private String[] getRationalRecipients(Teacher teacher) {
        ArrayList<String> rationalRecipientList = Lists.newArrayList();
        if (Objects.nonNull(teacher) && StringUtils.isNotEmpty(teacher.getEmail())) {
            String email = teacher.getEmail();
            if (email.contains(AT) && email.indexOf(AT) < email.length() - 2) {
                rationalRecipientList.add(email);
            }
        }
        if (!CollectionUtils.isEmpty(rationalRecipientList)) {
            return rationalRecipientList.toArray(new String[]{});
        }
        return null;
    }

}