package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanRequest {

    @NotNull(message = "计划号不能为空")
    private Long id;

    private String className;

    private String teacherName;

    private String courseName;

    private String week;

}
