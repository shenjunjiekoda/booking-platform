package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 11:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMessageRequest extends BaseRequest{

    private String createdAtFrom;

    private String createdAtTo;

}
