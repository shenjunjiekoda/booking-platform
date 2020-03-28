package cn.shenjunjie.booking.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 9:41
 */
@Data
public class SetSubscribeClassRequest {

    private List<Long> classIds;

}
