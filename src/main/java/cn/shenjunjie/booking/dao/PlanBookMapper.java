package cn.shenjunjie.booking.dao;

import java.util.List;

import cn.shenjunjie.booking.entity.PlanBook;
import cn.shenjunjie.booking.entity.PlanBookExample;
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

    PlanBook selectByPlanIdAndStatus(@Param("planId") Long planId,@Param("status") String status);

    int updateByExampleSelective(@Param("record") PlanBook record, @Param("example") PlanBookExample example);

    int updateByExample(@Param("record") PlanBook record, @Param("example") PlanBookExample example);

    int updateByPrimaryKeySelective(PlanBook record);

    int updateByPrimaryKey(PlanBook record);

    List<PlanBook> selectByClassIdAndStatus(@Param("classId") Long classId,@Param("status") String status);

    List<PlanBook> selectByClassId(@Param("classId") Long classId);

}