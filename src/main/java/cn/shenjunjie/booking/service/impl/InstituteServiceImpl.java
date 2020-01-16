package cn.shenjunjie.booking.service.impl;

import cn.shenjunjie.booking.dto.request.AddInstituteRequest;
import cn.shenjunjie.booking.dto.response.GetInstitutesResponse;
import cn.shenjunjie.booking.entity.Institute;
import cn.shenjunjie.booking.repo.InstituteRepo;
import cn.shenjunjie.booking.service.InstituteService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:48
 */
@Service
public class InstituteServiceImpl implements InstituteService {

    @Autowired
    private InstituteRepo instituteRepo;

    @Override
    public List<GetInstitutesResponse> getInstitutes() {
        List<GetInstitutesResponse> list = Lists.newArrayList();

        List<Institute> institutes = instituteRepo.selectAll();
        if(!CollectionUtils.isEmpty(institutes)){
            institutes.forEach( institute -> {
                GetInstitutesResponse response = new GetInstitutesResponse();
                BeanUtils.copyProperties(institute, response);
                list.add(response);
            });
        }

        return list;
    }

    @Override
    public void addInstitute(AddInstituteRequest request) {
        instituteRepo.insertByName(request.getName());
    }

}
