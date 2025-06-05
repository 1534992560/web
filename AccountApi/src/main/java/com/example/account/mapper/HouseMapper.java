package com.example.account.mapper;

import com.example.account.entity.House;
import com.example.account.entity.HouseExample;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HouseMapper {
    @Select({
        "<script>",
        "SELECT",
        "<if test='distinct'>",
        "  DISTINCT",
        "</if>",
        "id, admin_name, budget, create_time, update_time, is_delete",
        "FROM house",
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
        "</script>"
    })
    List<House> selectByExample(HouseExample example);

    @Select("SELECT id, admin_name, budget, create_time, update_time, is_delete " +
           "FROM house " +
           "WHERE id = #{id}")
    House selectByPrimaryKey(Integer id);

    @Select("SELECT id, admin_name, budget, create_time, update_time, is_delete " +
           "FROM house " +
           "WHERE admin_name = #{adminName}")
    House selectByAdminName(String adminName);

    @Delete("DELETE FROM house WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);

    @Insert("INSERT INTO house(id, admin_name, budget, create_time, update_time, is_delete) " +
           "VALUES(#{id}, #{adminName}, #{budget}, #{createTime}, #{updateTime}, #{isDelete})")
    int insert(House record);

    @Insert({
        "<script>",
        "INSERT INTO house",
        "<trim prefix='(' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>id,</if>",
        "  <if test='adminName != null'>admin_name,</if>",
        "  <if test='budget != null'>budget,</if>",
        "  <if test='createTime != null'>create_time,</if>",
        "  <if test='updateTime != null'>update_time,</if>",
        "  <if test='isDelete != null'>is_delete,</if>",
        "</trim>",
        "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>#{id,jdbcType=INTEGER},</if>",
        "  <if test='adminName != null'>#{adminName,jdbcType=VARCHAR},</if>",
        "  <if test='budget != null'>#{budget,jdbcType=REAL},</if>",
        "  <if test='createTime != null'>#{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>#{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='isDelete != null'>#{isDelete,jdbcType=INTEGER},</if>",
        "</trim>",
        "</script>"
    })
    int insertSelective(House record);

    @Update({
        "<script>",
        "UPDATE house",
        "<set>",
        "  <if test='adminName != null'>admin_name = #{adminName,jdbcType=VARCHAR},</if>",
        "  <if test='budget != null'>budget = #{budget,jdbcType=REAL},</if>",
        "  <if test='createTime != null'>create_time = #{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete,jdbcType=INTEGER},</if>",
        "</set>",
        "WHERE id = #{id,jdbcType=INTEGER}",
        "</script>"
    })
    int updateByPrimaryKeySelective(House record);

    @Update("UPDATE house SET " +
            "admin_name = #{adminName}, " +
            "budget = #{budget}, " +
            "create_time = #{createTime}, " +
            "update_time = #{updateTime}, " +
            "is_delete = #{isDelete} " +
            "WHERE id = #{id}")
    int updateByPrimaryKey(House record);
}