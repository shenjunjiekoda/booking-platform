package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.request.AddInstituteRequest;
import cn.shenjunjie.booking.dto.request.UpdateInstituteRequest;
import cn.shenjunjie.booking.dto.response.GetInstitutesResponse;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface InstituteService {

    List<GetInstitutesResponse> getInstitutes();

    String getInstituteByClassId(Long classId);

    void addInstitute(AddInstituteRequest request);

    void updateInstitute(UpdateInstituteRequest request);

}
