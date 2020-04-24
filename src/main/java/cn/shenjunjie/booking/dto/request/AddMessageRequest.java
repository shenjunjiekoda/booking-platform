package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/4/24 11:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMessageRequest {

    @NotEmpty(message = "至少有一个收件人！")
    private List<String> tos;

    @NotBlank(message = "消息不能为空！")
    private String msg;

}
