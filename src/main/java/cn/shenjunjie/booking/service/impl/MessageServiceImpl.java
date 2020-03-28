package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.GetMessageRequest;
import cn.shenjunjie.booking.dto.response.GetMessagesResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Message;
import cn.shenjunjie.booking.repo.MessageRepo;
import cn.shenjunjie.booking.repo.TeacherRepo;
import cn.shenjunjie.booking.service.MessageService;
import cn.shenjunjie.booking.utils.UserUtil;
import com.github.pagehelper.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

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
        ArrayList<GetMessagesResponse> list = Lists.newArrayList();
        Page<Message> messages = messageRepo.selectByRequestAndTo(request, to);
        if (CollectionUtils.isNotEmpty(messages)) {
            messages.forEach(message -> {
                GetMessagesResponse response = new GetMessagesResponse();
                BeanUtils.copyProperties(message, response);
                String from = teacherRepo.selectById(message.getFrom()).getName();
                response.setFrom(from);
                response.setData(message.getData());
                list.add(response);
            });
        }
        return new PageBean<>(request, list, messages.getTotal());
    }

    @Override
    public void read(Long id) {
        messageRepo.updateByIdAndReaded(id,1);
    }

    @Override
    public Long getUnReadedCount() {
        return messageRepo.countByReaded(0,UserUtil.getCurrentUser().getId());
    }
}
