package cn.shenjunjie.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GetPlanResponse {

    private Long id;

    private String courseName;

    private String className;

    private String teacherName;

    private Integer year;

    private Integer semester;

    private String week;

}
