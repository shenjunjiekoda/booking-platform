package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 21:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddClassRequest {

    @NotNull(message = "学院号不能为空")
    private Long instituteId;

    @NotBlank(message = "班级名不能为空")
    private String name;

}
