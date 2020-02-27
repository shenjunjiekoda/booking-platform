package cn.shenjunjie.booking.dto.response;

import cn.shenjunjie.booking.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/2/27 16:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlanBooksResponse {

    private Long id;

    private Long classId;

    private String className;

    private Long courseId;

    private String courseName;

    private Long teacherId;

    private String teacherName;

    private Long bookId;

    private String bookName;

    private Long stuNum;

    private Long teacherNum;

    private String status;

    private String week;

}
