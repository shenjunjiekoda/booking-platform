package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPlanBookRequest {

    @NotNull(message = "计划号不能为空")
    private Long planId;

    @NotEmpty(message = "书列表不能为空")
    private List<Long> bookIds;

    private Long teacherNum;

    private Long stuNum;

}
