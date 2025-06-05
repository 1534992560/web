package com.example.account.mapper;

import com.example.account.entity.User;
import com.example.account.entity.UserExample;
import java.util.List;

import com.example.account.param.TransferUserParam;
import com.example.account.param.UpdateUserParam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    @Insert("INSERT INTO user(name, password, is_admin, house_id, email, create_time, update_time, is_delete) " +
           "VALUES(#{name}, #{password}, #{isAdmin}, #{houseId}, #{email}, #{createTime}, #{updateTime}, #{isDelete})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User record);

    @Insert({
        "<script>",
        "INSERT INTO user",
        "<trim prefix='(' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>id,</if>",
        "  <if test='name != null'>name,</if>",
        "  <if test='password != null'>password,</if>",
        "  <if test='isAdmin != null'>is_admin,</if>",
        "  <if test='houseId != null'>house_id,</if>",
        "  <if test='email != null'>email,</if>",
        "  <if test='createTime != null'>create_time,</if>",
        "  <if test='updateTime != null'>update_time,</if>",
        "  <if test='isDelete != null'>is_delete,</if>",
        "</trim>",
        "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>#{id,jdbcType=INTEGER},</if>",
        "  <if test='name != null'>#{name,jdbcType=VARCHAR},</if>",
        "  <if test='password != null'>#{password,jdbcType=VARCHAR},</if>",
        "  <if test='isAdmin != null'>#{isAdmin,jdbcType=INTEGER},</if>",
        "  <if test='houseId != null'>#{houseId,jdbcType=INTEGER},</if>",
        "  <if test='email != null'>#{email,jdbcType=VARCHAR},</if>",
        "  <if test='createTime != null'>#{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>#{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='isDelete != null'>#{isDelete,jdbcType=INTEGER},</if>",
        "</trim>",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(User record);

    @Select({
        "<script>",
        "SELECT",
        "<if test='distinct'>",
        "  DISTINCT",
        "</if>",
        "id, name, password, is_admin, house_id, create_time, update_time, is_delete, email",
        "FROM user",
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
    List<User> selectByExample(UserExample example);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectByPrimaryKey(Integer id);

    @Select("SELECT * FROM user WHERE name = #{name} AND is_delete = 0")
    User selectByName(String name);

    @Select("SELECT * FROM user WHERE password = #{password} AND is_delete = 0")
    User selectByPsw(String password);

    @Select("SELECT * FROM user WHERE house_id = #{houseId} AND is_delete = 0")
    List<User> selectByHouseId(Integer houseId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    @Update({
        "<script>",
        "UPDATE user",
        "<set>",
        "  <if test='name != null'>name = #{name},</if>",
        "  <if test='password != null'>password = #{password},</if>",
        "  <if test='isAdmin != null'>is_admin = #{isAdmin},</if>",
        "  <if test='houseId != null'>house_id = #{houseId},</if>",
        "  <if test='email != null'>email = #{email},</if>",
        "  <if test='createTime != null'>create_time = #{createTime},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete},</if>",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateByPrimaryKeySelective(User record);

    @Update("UPDATE user SET " +
            "name = #{name}, " +
            "password = #{password}, " +
            "is_admin = #{isAdmin}, " +
            "house_id = #{houseId}, " +
            "email = #{email}, " +
            "create_time = #{createTime}, " +
            "update_time = #{updateTime}, " +
            "is_delete = #{isDelete} " +
            "WHERE id = #{id}")
    int updateByPrimaryKey(User record);

    @Update("UPDATE user SET password = #{newPassword}, update_time = now() WHERE id = #{id}")
    int updateMyPsw(UpdateUserParam param);

    @Update("UPDATE user SET is_delete = 1, update_time = now() WHERE id = #{removeUserId}")
    int removeUser(Integer removeUserId);

    @Update("UPDATE user SET is_admin = #{isAdmin}, update_time = now() WHERE id = #{id} AND is_delete = 0")
    int transferAdmin(@Param("id") Integer id, @Param("isAdmin") Integer isAdmin);
}