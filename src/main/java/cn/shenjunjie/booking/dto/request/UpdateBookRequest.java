package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 8:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequest {

    @NotNull(message = "书号不能为空")
    private Long id;

    private String name;

    private String isbn;

    private String author;

    private String press;

    private Integer publishedAtYear;

    private Integer publishedAtMonth;

    private Long edition;

    private String remark;

}
