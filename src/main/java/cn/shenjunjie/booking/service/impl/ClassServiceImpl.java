package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.AddClassRequest;
import cn.shenjunjie.booking.dto.request.UpdateClassRequest;
import cn.shenjunjie.booking.dto.response.GetClassesResponse;
import cn.shenjunjie.booking.entity.Class;
import cn.shenjunjie.booking.repo.ClassRepo;
import cn.shenjunjie.booking.service.ClassService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassRepo classRepo;

    @Override
    public List<GetClassesResponse> getClasses(Long instituteId) {
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

    @Transactional
    @Override
    public void addClass(AddClassRequest request) {
        classRepo.insertByInstituteIdAndName(request.getInstituteId(), request.getName());
    }

    @Transactional
    @Override
    public void updateClass(UpdateClassRequest request) {
        classRepo.updateByIdAndName(request.getId(), request.getName());
    }

    @Transactional
    @Override
    public void deleteClass(Long id) {
        classRepo.deleteById(id);
    }

}
