package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.GetSubscribeLogRequest;
import cn.shenjunjie.booking.dto.request.SetSubscribeClassRequest;
import cn.shenjunjie.booking.dto.response.GetSubscribeMsgResponse;
import cn.shenjunjie.booking.dto.response.PageBean;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.entity.OperationLog;
import cn.shenjunjie.booking.entity.Teacher;
import cn.shenjunjie.booking.entity.TeacherClass;
import cn.shenjunjie.booking.repo.ClassRepo;
import cn.shenjunjie.booking.repo.TeacherClassRepo;
import cn.shenjunjie.booking.service.OperationLogService;
import cn.shenjunjie.booking.service.SubscribeService;
import cn.shenjunjie.booking.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:35
 */
@Slf4j
@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Resource
    private OperationLogService operationLogService;
    @Resource
    private TeacherClassRepo teacherClassRepo;
    @Resource
    private ClassRepo classRepo;

    @Override
    public PageBean<GetSubscribeMsgResponse> getMsg(GetSubscribeLogRequest request) {
        Teacher teacher = UserUtil.getCurrentUser();
        List<Long> classIds = Lists.newArrayList();
        List<OperationLog> logs = Lists.newArrayList();
        ArrayList<GetSubscribeMsgResponse> list = Lists.newArrayList();
        if(teacher!=null && request.getClassId()==null){
            List<TeacherClass> teacherClasses = teacherClassRepo.selectByTeacherId(teacher.getId());
            classIds = teacherClasses.stream().map(TeacherClass::getClassId).collect(Collectors.toList());
        } else if(teacher!=null && request.getClassId()!=null){
            classIds.add(request.getClassId());
        }
        if(CollectionUtils.isNotEmpty(classIds)){
            List<OperationLog> finalLogs = logs;
            classIds.forEach(classId ->{
                if(StringUtils.isNoneBlank(request.getCreatedAtFrom(),request.getCreatedAtTo())){
                    List<OperationLog> tmp = operationLogService.getLogs(classId,request.getCreatedAtFrom(),request.getCreatedAtTo());
                    finalLogs.addAll(tmp);
                }else{
                    List<OperationLog> tmp = operationLogService.getLogs(classId);
                    finalLogs.addAll(tmp);
                }
            });
        }
        if(CollectionUtils.isNotEmpty(logs)) {
            logs.stream().skip(request.getPageSize() * (request.getPageCurrent() - 1))
                    .limit(request.getPageSize()).forEach(log->{
                GetSubscribeMsgResponse response = new GetSubscribeMsgResponse();
                response.setId(log.getId());
                response.setMsg(log.getMsg());
                response.setCreatedAt(log.getCreatedAt());
                response.setType(log.getType());
                list.add(response);
            });
        }
        return new PageBean<>(request, list, list.size());
    }

    @Transactional
    @Override
    public void setSubscribeClasses(SetSubscribeClassRequest request) {
        Teacher teacher = UserUtil.getCurrentUser();
        if(teacher!=null){
            teacherClassRepo.deleteByTeacherId(teacher.getId());
            if(CollectionUtils.isNotEmpty(request.getClassIds())){
                request.getClassIds().forEach(classId ->{
                    teacherClassRepo.insertByTeacherIdAndClassId(teacher.getId(), classId);
                });
            }
        }
    }

    @Override
    public List<Class> getSubscribeClasses() {
        Long teacherId = UserUtil.getCurrentUser().getId();
        List<Class> list = Lists.newArrayList();
        List<TeacherClass> teacherClasses = teacherClassRepo.selectByTeacherId(teacherId);
        if(CollectionUtils.isNotEmpty(teacherClasses)){
            list = teacherClasses.stream().map(TeacherClass::getClassId).map(classRepo::selectById).collect(Collectors.toList());
        }
        return list;
    }
}
