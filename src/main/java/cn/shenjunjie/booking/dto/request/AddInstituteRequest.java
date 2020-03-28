package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 7:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddInstituteRequest {

    @NotBlank(message = "学院名不能为空")
    private String name;

}
