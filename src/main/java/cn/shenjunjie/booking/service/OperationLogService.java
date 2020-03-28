package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.entity.OperationLog;
import cn.shenjunjie.booking.enums.OperationType;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:23
 */
public interface OperationLogService {

    List<OperationLog> getLogs(Long classId);

    List<OperationLog> getLogs(Long classId, String createdAtFrom,String createdAtTo);

    void insert(Long classId, OperationType type, String msg);

}
