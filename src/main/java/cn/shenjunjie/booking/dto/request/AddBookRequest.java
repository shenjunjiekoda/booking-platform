package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/15 8:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBookRequest {

    @NotBlank(message = "书名不能为空！")
    private String name;

    @NotBlank(message = "isbn号不能为空！")
    private String isbn;

    private String author;

    private String press;

    private String publishedAt;

    private Long edition;

    private String remark;

}
