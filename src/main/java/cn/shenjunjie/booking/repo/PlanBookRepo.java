package cn.shenjunjie.booking.repo;

import cn.shenjunjie.booking.dao.PlanBookMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/1/12 17:51
 */
@Repository
public class PlanBookRepo {

    @Resource
    private PlanBookMapper planBookMapper;

}
