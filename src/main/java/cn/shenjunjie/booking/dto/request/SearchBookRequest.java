package cn.shenjunjie.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/4/24 16:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookRequest {

    private String bookName;

    private String author;

    private String press;

    private String isbn;

}
