package cn.shenjunjie.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/3 16:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginResponse {

    private String name;

    private String uuid;

    private String token;

    private String teacherId;

    private String email;

    private Long expireTime;

}
