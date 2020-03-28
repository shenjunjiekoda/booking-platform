package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.request.GetSubscribeLogRequest;
import cn.shenjunjie.booking.dto.request.SetSubscribeClassRequest;
import cn.shenjunjie.booking.dto.response.GetSubscribeMsgResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Class;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:35
 */
public interface SubscribeService {

    PageBean<GetSubscribeMsgResponse> getMsg(GetSubscribeLogRequest request);

    void setSubscribeClasses(SetSubscribeClassRequest request);

    List<Class> getSubscribeClasses();
}
