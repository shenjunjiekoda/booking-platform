package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.AddMessageRequest;
import cn.shenjunjie.booking.dto.request.GetMessageRequest;
import cn.shenjunjie.booking.dto.response.GetMessagesResponse;
import cn.shenjunjie.booking.dto.response.PageBean;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 11:51
 */
public interface MessageService {

    PageBean<GetMessagesResponse> getMessages(GetMessageRequest request);

    void read(Long id);

    void readAll();

    Long getUnReadedCount();

    RestBody addMsg(AddMessageRequest request);

}
