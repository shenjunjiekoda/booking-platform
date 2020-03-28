package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.response.GetCurProfileResponse;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface TeacherService {

    GetCurProfileResponse getCurProfile();

    List<String> getTeacherNameByKeyword(String keyword);
}
