package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/4/23 22:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlanBookRequest {

    @NotNull(message = "计划号不能为空")
    private Long planId;

    @NotNull(message = "老师订购数量不能为空")
    private Long teacherNum;

    @NotNull(message = "学生订购数量不能为空")
    private Long stuNum;

    @NotBlank(message = "书名不能为空")
    private String bookName;

}
