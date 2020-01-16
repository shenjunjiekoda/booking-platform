package cn.shenjunjie.booking.service;

import cn.shenjunjie.booking.dto.request.AddInstituteRequest;
import cn.shenjunjie.booking.dto.response.GetInstitutesResponse;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:47
 */
public interface InstituteService {

    List<GetInstitutesResponse> getInstitutes();

    void addInstitute(AddInstituteRequest request);

}
