package cn.shenjunjie.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/15 8:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GetBooksFromWebResponse {

    private String name;

    private String isbn;

    private String author;

    private String press;

    private String publishedAt;

}
