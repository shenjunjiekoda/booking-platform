package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/17 19:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInstituteRequest {

    @NotNull(message = "学院号不能为空")
    private Long id;

    @NotBlank(message = "学院名不能为空")
    private String name;

}
