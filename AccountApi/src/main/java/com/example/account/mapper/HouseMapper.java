package com.example.account.mapper;

import com.example.account.entity.House;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HouseMapper {
    @Select("SELECT * FROM house WHERE id = #{id}")
    House selectByPrimaryKey(Integer id);

    @Select("SELECT * FROM house WHERE admin_name = #{adminName} AND is_delete = 0")
    House selectByAdminName(String adminName);

    @Insert("INSERT INTO house(admin_name, budget, create_time, update_time, is_delete) " +
           "VALUES(#{adminName}, #{budget}, #{createTime}, #{updateTime}, #{isDelete})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(House record);

    @Update({
        "<script>",
        "UPDATE house",
        "<set>",
        "  <if test='adminName != null'>admin_name = #{adminName},</if>",
        "  <if test='budget != null'>budget = #{budget},</if>",
        "  <if test='createTime != null'>create_time = #{createTime},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete},</if>",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateByPrimaryKeySelective(House record);
}