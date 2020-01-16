package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.AddClassRequest;
import cn.shenjunjie.booking.dto.request.DeleteClassRequest;
import cn.shenjunjie.booking.dto.request.UpdateClassRequest;
import cn.shenjunjie.booking.dto.response.GetClassesResponse;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.repo.ClassRepo;
import cn.shenjunjie.booking.service.ClassService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassRepo classRepo;

    @Override
    public List<GetClassesResponse> getClasses(Long instituteId) {
        if (Objects.isNull(instituteId)) {
            return null;
        }
        List<GetClassesResponse> classesResponseList = Lists.newArrayList();

        List<Class> list = classRepo.selectByInstituteId(instituteId);
        if (CollectionUtils.isEmpty(list)) {
            list.forEach(item -> {
                GetClassesResponse response = new GetClassesResponse();
                BeanUtils.copyProperties(item, response);
                classesResponseList.add(response);
            });
        }
        return classesResponseList;
    }

    @Override
    public void addClass(AddClassRequest request) {
        if (Objects.nonNull(request) && Objects.nonNull(request.getInstituteId()) && !StringUtils.isEmpty(request.getName())) {
            classRepo.insertByInstituteIdAndName(request.getInstituteId(), request.getName());
        }
    }

    @Override
    public void updateClass(UpdateClassRequest request) {
        if (Objects.nonNull(request) && Objects.nonNull(request.getId()) && !StringUtils.isEmpty(request.getName())) {
            classRepo.updateByIdAndName(request.getId(), request.getName());
        }
    }

    @Override
    public void deleteClass(DeleteClassRequest request) {
        if (Objects.nonNull(request) && Objects.nonNull(request.getId())) {
            classRepo.deleteById(request.getId());
        }
    }

}
