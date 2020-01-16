package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.CourseMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class CourseRepo {

    @Resource
    private CourseMapper courseMapper;

}
