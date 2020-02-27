package cn.shenjunjie.booking.dao;

import cn.shenjunjie.booking.entity.PlanBook;
import cn.shenjunjie.booking.entity.PlanBookExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author JunjieShen
 */
public interface PlanBookMapper {
    long countByExample(PlanBookExample example);

    int deleteByExample(PlanBookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PlanBook record);

    int insertSelective(PlanBook record);

    List<PlanBook> selectByExample(PlanBookExample example);

    PlanBook selectByPrimaryKey(Long id);

    PlanBook selectByPlanId(@Param("planId") Long planId);

    int updateByExampleSelective(@Param("record") PlanBook record, @Param("example") PlanBookExample example);

    int updateByExample(@Param("record") PlanBook record, @Param("example") PlanBookExample example);

    int updateByPrimaryKeySelective(PlanBook record);

    int updateByPrimaryKey(PlanBook record);
}