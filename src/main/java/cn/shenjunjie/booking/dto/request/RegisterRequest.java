package cn.shenjunjie.booking.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/23 19:42
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "姓名不能为空")
    public String name;

    @NotBlank(message = "工号不能为空")
    public String teacherId;

    @NotBlank(message = "密码不能为空")
    public String password;

    @NotBlank(message = "邮箱不能为空")
    public String email;

    @NotNull(message = "判断是否是教材部人员不能为空")
    public Integer isAdmin;
}
