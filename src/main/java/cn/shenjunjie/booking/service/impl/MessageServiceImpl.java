package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddMessageRequest;
import cn.shenjunjie.booking.dto.request.GetMessageRequest;
import cn.shenjunjie.booking.dto.response.GetMessagesResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Message;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.repo.MessageRepo;
import cn.shenjunjie.booking.repo.TeacherRepo;
import cn.shenjunjie.booking.service.MessageService;
import cn.shenjunjie.booking.utils.UserUtil;
import com.github.pagehelper.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 11:56
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageRepo messageRepo;
    @Resource
    private TeacherRepo teacherRepo;

    @Override
    public PageBean<GetMessagesResponse> getMessages(GetMessageRequest request) {
        Long to = UserUtil.getCurrentUser().getId();
        List<GetMessagesResponse> list = Lists.newArrayList();
        Page<Message> messages = messageRepo.selectByRequestAndTo(request, to);
        if (CollectionUtils.isNotEmpty(messages)) {
            List<GetMessagesResponse> finalList = list;
            messages.forEach(message -> {
                GetMessagesResponse response = new GetMessagesResponse();
                BeanUtils.copyProperties(message, response);
                String from = teacherRepo.selectById(message.getFrom()).getName();
                response.setFrom(from);
                response.setData(message.getData());
                finalList.add(response);
            });
        }
        if (!CollectionUtils.isEmpty(list)) {
            list = list.stream().sorted((o1, o2) -> {
                if (o1.getReaded().equals(o2.getReaded())) {
                    return o2.getCreatedat().compareTo(o1.getCreatedat());
                } else {
                    return o1.getReaded() - o2.getReaded();
                }
            }).collect(Collectors.toList());
        }
        return new PageBean<>(request, list, messages.getTotal());
    }

    @Override
    public void read(Long id) {
        messageRepo.updateByIdAndReaded(id, 1);
    }

    @Override
    public void readAll() {
        List<Message> messages = messageRepo.selectByToAndReaded(UserUtil.getCurrentUser().getId(), 0);
        if (CollectionUtils.isEmpty(messages)) {
            return;
        }
        messages.forEach(message -> messageRepo.updateByIdAndReaded(message.getId(), 1));
    }

    @Override
    public Long getUnReadedCount() {
        return messageRepo.countByReaded(0, UserUtil.getCurrentUser().getId());
    }

    @Override
    public RestBody addMsg(AddMessageRequest request) {
        Teacher user = UserUtil.getCurrentUser();
        if (user == null || user.getId() == null) {
            return RestBody.fail("发件人异常, 发送失败");
        }
        if (StringUtils.isEmpty(request.getMsg().trim())) {
            return RestBody.fail("消息不允许为空！");
        }
        request.getTos().stream().map(teacherRepo::selectByName).forEach(to -> {
            Message record = new Message();
            record.setFrom(user.getId());
            record.setTo(to.getId());
            record.setData(request.getMsg().trim());
            messageRepo.insertByRecord(record);
        });
        return RestBody.succeed();
    }
}
