package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlanBookRequest {

    @NotNull(message = "计划号不能为空")
    private Long planBookId;

    @NotBlank(message = "书名不能为空")
    private String bookName;

    @NotNull(message = "老师订书数不能为空")
    private Long teacherNum;

    @NotNull(message = "学生订书数不能为空")
    private Long stuNum;

    @NotNull(message = "实际数量不能为空")
    private Integer actualNum;

}
