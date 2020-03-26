package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/26 16:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlanBookRequest extends BaseRequest {

    @NotNull(message = "班级不能为空！")
    private Long classId;

    @NotBlank(message = "状态不能为空！")
    private String status;

    @NotNull(message = "学年不能为空！")
    private Integer year;

    @NotNull(message = "学期不能为空！")
    private Integer semester;

}
