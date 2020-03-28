package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlanRequest extends BaseRequest {

    private String courseName;

    private String className;

    private String TeacherName;

    private Integer year;

    private Integer semester;

    private Integer weekStart;

    private Integer weekEnd;

}
