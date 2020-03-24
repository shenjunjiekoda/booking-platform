package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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

    @NotBlank(message = "作者不能为空!")
    private String author;

    @NotBlank(message = "出版社不能为空!")
    private String press;

    @NotNull(message = "出版年份不能为空!")
    private Integer publishedAtYear;

    @NotNull(message = "出版月份不能为空!")
    private Integer publishedAtMonth;

    private Long edition;

    //add
    private String remark;

}
