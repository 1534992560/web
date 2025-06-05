package com.example.account.mapper;

import com.example.account.entity.ConsumptionType;
import com.example.account.entity.ConsumptionTypeExample;
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

    @Select({
        "<script>",
        "SELECT",
        "<if test='distinct'>",
        "  DISTINCT",
        "</if>",
        "id, name, create_time, update_time, house_id, is_custom, is_delete",
        "FROM consumption_type",
        "<if test='_parameter != null'>",
        "  <where>",
        "    <foreach collection='oredCriteria' item='criteria' separator='or'>",
        "      <if test='criteria.valid'>",
        "        <trim prefix='(' suffix=')' prefixOverrides='and'>",
        "          <foreach collection='criteria.criteria' item='criterion'>",
        "            <choose>",
        "              <when test='criterion.noValue'>",
        "                AND ${criterion.condition}",
        "              </when>",
        "              <when test='criterion.singleValue'>",
        "                AND ${criterion.condition} #{criterion.value}",
        "              </when>",
        "              <when test='criterion.betweenValue'>",
        "                AND ${criterion.condition} #{criterion.value} AND #{criterion.secondValue}",
        "              </when>",
        "              <when test='criterion.listValue'>",
        "                AND ${criterion.condition}",
        "                <foreach collection='criterion.value' item='listItem' open='(' close=')' separator=','>",
        "                  #{listItem}",
        "                </foreach>",
        "              </when>",
        "            </choose>",
        "          </foreach>",
        "        </trim>",
        "      </if>",
        "    </foreach>",
        "  </where>",
        "</if>",
        "<if test='orderByClause != null'>",
        "  ORDER BY ${orderByClause}",
        "</if>",
        "WHERE is_delete = 0",
        "</script>"
    })
    List<ConsumptionType> selectByExample(ConsumptionTypeExample example);

    @Select("SELECT id, name, create_time, update_time, house_id, is_custom, is_delete " +
           "FROM consumption_type " +
           "WHERE id = #{id}")
    ConsumptionType selectByPrimaryKey(Integer id);

    @Delete("DELETE FROM consumption_type WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);

    @Insert("INSERT INTO consumption_type(id, name, create_time, update_time, house_id, is_custom, is_delete) " +
           "VALUES(#{id}, #{name}, #{createTime}, #{updateTime}, #{houseId}, #{isCustom}, #{isDelete})")
    int insert(ConsumptionType record);

    @Insert({
        "<script>",
        "INSERT INTO consumption_type",
        "<trim prefix='(' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>id,</if>",
        "  <if test='name != null'>name,</if>",
        "  <if test='createTime != null'>create_time,</if>",
        "  <if test='updateTime != null'>update_time,</if>",
        "  <if test='houseId != null'>house_id,</if>",
        "  <if test='isCustom != null'>is_custom,</if>",
        "  <if test='isDelete != null'>is_delete,</if>",
        "</trim>",
        "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>#{id,jdbcType=INTEGER},</if>",
        "  <if test='name != null'>#{name,jdbcType=VARCHAR},</if>",
        "  <if test='createTime != null'>#{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>#{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='houseId != null'>#{houseId,jdbcType=INTEGER},</if>",
        "  <if test='isCustom != null'>#{isCustom,jdbcType=INTEGER},</if>",
        "  <if test='isDelete != null'>#{isDelete,jdbcType=INTEGER},</if>",
        "</trim>",
        "</script>"
    })
    int insertSelective(ConsumptionType record);

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

    @Update("UPDATE consumption_type SET " +
            "name = #{name}, " +
            "create_time = #{createTime}, " +
            "update_time = #{updateTime}, " +
            "house_id = #{houseId}, " +
            "is_custom = #{isCustom}, " +
            "is_delete = #{isDelete} " +
            "WHERE id = #{id}")
    int updateByPrimaryKey(ConsumptionType record);
}