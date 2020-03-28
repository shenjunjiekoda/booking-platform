package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.entity.OperationLog;
import cn.shenjunjie.booking.enums.OperationType;
import cn.shenjunjie.booking.repo.OperationLogRepo;
import cn.shenjunjie.booking.service.OperationLogService;
import cn.shenjunjie.booking.utils.DateUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:24
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogRepo operationLogRepo;

    @Override
    public List<OperationLog> getLogs(Long classId) {
        return operationLogRepo.selectByClassId(classId);
    }

    @Override
    public List<OperationLog> getLogs(Long classId, String createdAtFrom, String createdAtTo) {
        Date from = DateUtil.formatDateByString(createdAtFrom);
        Date to = DateUtil.formatDateByString(createdAtTo);
        return operationLogRepo.selectByClassIdAndCreatedAtFromAndCreatedAtTo(classId, from, to);
    }

    @Async
    @Override
    public void insert(Long classId, OperationType type, String msg) {
        operationLogRepo.insertByClassIdAndTypeAndMsg(classId, type, msg);
    }

}
