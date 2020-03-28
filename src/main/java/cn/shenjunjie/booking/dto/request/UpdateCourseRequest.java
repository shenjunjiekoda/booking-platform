package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    @NotNull(message = "课程号不能为空")
    private Long id;

    @NotBlank(message = "课程名不能为空")
    private String name;

}
