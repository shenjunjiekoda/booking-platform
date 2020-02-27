package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.request.AddClassRequest;
import cn.shenjunjie.booking.dto.request.DeleteClassRequest;
import cn.shenjunjie.booking.dto.request.UpdateClassRequest;
import cn.shenjunjie.booking.dto.response.GetClassesResponse;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface ClassService {

    /**
     * 根据学院查找班级
     * @param instituteId
     * @return
     */
    List<GetClassesResponse> getClasses(Long instituteId);

    /**
     * 增加一个班级信息
     * @param request
     */
    void addClass(AddClassRequest request);

    /**
     * 更新一个班级信息
     * @param request
     */
    void updateClass(UpdateClassRequest request);

    /**
     * 删除一个班级信息
     */
    void deleteClass(Long id);

}
