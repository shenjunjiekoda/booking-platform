package cn.shenjunjie.booking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/27 11:52
 */
@Data
public class GetMessagesResponse {

    private Long id;

    private String from;

    private Integer readed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdat;

    private String data;

}
