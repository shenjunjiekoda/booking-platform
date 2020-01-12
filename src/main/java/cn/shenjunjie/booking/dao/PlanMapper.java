package cn.shenjunjie.booking.dao;

import cn.shenjunjie.booking.entity.Plan;
import cn.shenjunjie.booking.entity.PlanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author JunjieShen
 */
public interface PlanMapper {
    long countByExample(PlanExample example);

    int deleteByExample(PlanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Plan record);

    int insertSelective(Plan record);

    List<Plan> selectByExample(PlanExample example);

    Plan selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Plan record, @Param("example") PlanExample example);

    int updateByExample(@Param("record") Plan record, @Param("example") PlanExample example);

    int updateByPrimaryKeySelective(Plan record);

    int updateByPrimaryKey(Plan record);
}