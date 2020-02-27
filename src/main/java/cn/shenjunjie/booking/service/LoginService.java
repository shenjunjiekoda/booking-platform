package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.common.rest.RestBody;
import cn.shenjunjie.booking.dto.request.LoginRequest;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/15 8:53
 */
public interface LoginService {

    RestBody login(LoginRequest request);

    RestBody logout();

}
