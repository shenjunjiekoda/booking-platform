package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/16 8:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookRequest {

    private Long id;

    private String name;

    private String isbn;

    private String author;

    private String press;

    private String publishedAt;

    private Long edition;

    private String remark;

}
