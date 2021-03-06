package cn.shenjunjie.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 21:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GetClassesResponse {

    private Long id;

    private String name;

}
