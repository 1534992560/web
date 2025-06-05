package com.example.account.mapper;

import com.example.account.entity.ConsumptionType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ConsumptionTypeMapper {
    @Select("SELECT id, name, create_time, update_time, house_id, is_custom, is_delete " +
           "FROM consumption_type " +
           "WHERE name = #{name} AND is_delete = 0")
    ConsumptionType selectByName(String name);

    @Select("SELECT id, name, create_time, update_time, house_id, is_custom, is_delete " +
           "FROM consumption_type " +
           "WHERE (house_id = 0 OR house_id = #{houseId}) AND is_delete = 0")
    List<ConsumptionType> selectByHouseId(Integer houseId);



    @Select("SELECT id, name, create_time, update_time, house_id, is_custom, is_delete " +
           "FROM consumption_type " +
           "WHERE id = #{id}")
    ConsumptionType selectByPrimaryKey(Integer id);



    @Insert("INSERT INTO consumption_type(id, name, create_time, update_time, house_id, is_custom, is_delete) " +
           "VALUES(#{id}, #{name}, #{createTime}, #{updateTime}, #{houseId}, #{isCustom}, #{isDelete})")
    int insert(ConsumptionType record);


    @Update({
        "<script>",
        "UPDATE consumption_type",
        "<set>",
        "  <if test='name != null'>name = #{name,jdbcType=VARCHAR},</if>",
        "  <if test='createTime != null'>create_time = #{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='houseId != null'>house_id = #{houseId,jdbcType=INTEGER},</if>",
        "  <if test='isCustom != null'>is_custom = #{isCustom,jdbcType=INTEGER},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete,jdbcType=INTEGER},</if>",
        "</set>",
        "WHERE id = #{id,jdbcType=INTEGER}",
        "</script>"
    })
    int updateByPrimaryKeySelective(ConsumptionType record);

}