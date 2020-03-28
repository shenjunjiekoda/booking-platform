package cn.shenjunjie.booking.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:21
 */
@Data
public class GetSubscribeLogRequest extends BaseRequest{

    private Long classId;

    private String createdAtFrom;

    private String createdAtTo;

}
