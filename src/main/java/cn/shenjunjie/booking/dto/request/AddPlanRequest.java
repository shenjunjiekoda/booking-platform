package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlanRequest {

    @NotBlank(message = "老师名不能为空")
    private String teacherName;

    @NotBlank(message = "课程名不能为空")
    private String courseName;

    @NotBlank(message = "班级不能为空")
    private String className;

    private String week;

}
