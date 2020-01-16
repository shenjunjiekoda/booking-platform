package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 21:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddClassRequest {

    private Long instituteId;

    private String name;

}
