package com.example.account.mapper;

import com.example.account.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.account.param.TransferUserParam;
import com.example.account.param.UpdateUserParam;

@Mapper
@Repository
public interface UserMapper {

    @Insert("INSERT INTO user(name, password, is_admin, house_id, create_time, update_time, is_delete, email) " +
           "VALUES(#{name}, #{password}, #{isAdmin}, #{houseId}, #{createTime}, #{updateTime}, #{isDelete}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(User record);

    @Select("SELECT * FROM user WHERE name = #{name} AND is_delete = 0")
    User selectByName(String name);

    @Select("SELECT * FROM user WHERE house_id = #{houseId} AND is_delete = 0")
    List<User> selectByHouseId(Integer houseId);

    @Select("SELECT * FROM user WHERE email = #{email} AND is_delete = 0")
    User selectByEmail(String email);

    @Select("SELECT * FROM user WHERE name = #{name} AND password = #{password} AND is_delete = 0")
    List<User> selectByNameAndPassword(@Param("name") String name, @Param("password") String password);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectByPrimaryKey(Integer id);

    @Select("SELECT * FROM user WHERE name = #{name} AND password = #{password} AND is_delete = 0")
    User selectByNameAndPasswordForUpdate(@Param("name") String name, @Param("password") String password);

    @Update({
        "<script>",
        "UPDATE user",
        "<set>",
        "  <if test='name != null'>name = #{name},</if>",
        "  <if test='password != null'>password = #{password},</if>",
        "  <if test='isAdmin != null'>is_admin = #{isAdmin},</if>",
        "  <if test='houseId != null'>house_id = #{houseId},</if>",
        "  <if test='createTime != null'>create_time = #{createTime},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete},</if>",
        "  <if test='email != null'>email = #{email},</if>",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateByPrimaryKeySelective(User record);

    @Update("UPDATE user SET password = #{newPassword}, update_time = now() WHERE id = #{id}")
    int updateMyPsw(UpdateUserParam param);

    @Update("UPDATE user SET is_delete = 1, update_time = now() WHERE id = #{removeUserId}")
    int removeUser(Integer removeUserId);

    @Update("UPDATE user SET is_admin = #{isAdmin} WHERE id = #{id}")
    int transferAdmin(@Param("id") Integer id, @Param("isAdmin") Integer isAdmin);
}