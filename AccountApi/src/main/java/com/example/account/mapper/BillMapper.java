package com.example.account.mapper;

import com.example.account.dto.QueryBillsDTO;
import com.example.account.dto.StatisticsByTypeDTO;
import com.example.account.entity.Bill;
import com.example.account.param.BillManageParam;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Mapper
@Repository
public interface BillMapper {
    @Select("SELECT * FROM bill WHERE id = #{id}")
    Bill selectByPrimaryKey(Integer id);

    @Select("SELECT * FROM bill WHERE house_id = #{houseId} AND is_delete = 0 ORDER BY create_time DESC")
    List<Bill> selectByHouseId(Integer houseId);

    @Select("SELECT * FROM bill WHERE house_id = #{houseId} AND create_time BETWEEN #{startTime} AND #{endTime} AND is_delete = 0")
    List<Bill> selectByTimeRange(@Param("houseId") Integer houseId, 
                                @Param("startTime") Date startTime, 
                                @Param("endTime") Date endTime);

    @Select("SELECT * FROM bill WHERE house_id = #{houseId} AND consumption_id = #{typeId} " +
            "AND create_time BETWEEN #{startTime} AND #{endTime} AND is_delete = 0")
    List<Bill> selectByTypeAndTimeRange(@Param("houseId") Integer houseId,
                                       @Param("typeId") Integer typeId,
                                       @Param("startTime") Date startTime,
                                       @Param("endTime") Date endTime);

    @Select("SELECT SUM(money) FROM bill WHERE house_id = #{houseId} " +
            "AND create_time BETWEEN #{startTime} AND #{endTime} AND is_delete = 0")
    Double sumMoneyByTimeRange(@Param("houseId") Integer houseId,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime);

    @Select("SELECT SUM(money) FROM bill WHERE house_id = #{houseId} AND consumption_id = #{typeId} " +
            "AND create_time BETWEEN #{startTime} AND #{endTime} AND is_delete = 0")
    Double sumMoneyByTypeAndTimeRange(@Param("houseId") Integer houseId,
                                     @Param("typeId") Integer typeId,
                                     @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime);

    @Select({
        "<script>",
        "SELECT",
        "  a.id id,",
        "  a.money money,",
        "  a.consumption_id consumptionId,",
        "  b.name consumptionName,",
        "  b.is_custom isCustom,",
        "  a.remark remark,",
        "  a.record_time recordTime,",
        "  a.user_id userId,",
        "  c.name userName,",
        "  a.house_id houseId,",
        "  a.bill_type billType",
        "FROM bill a",
        "LEFT JOIN consumption_type b ON a.consumption_id = b.id AND b.is_delete = 0",
        "LEFT JOIN user c ON c.id = a.user_id",
        "WHERE a.is_delete = 0",
        "  AND a.house_id = #{houseId}",
        "  <if test='userId != null'>",
        "    AND a.user_id = #{userId}",
        "  </if>",
        "  <if test='recordId != null'>",
        "    AND a.user_id = #{recordId}",
        "  </if>",
        "  <if test='billType != null'>",
        "    AND a.bill_type = #{billType}",
        "  </if>",
        "  <if test='remark != null and remark != \"\"'>",
        "    AND a.remark LIKE CONCAT('%', #{remark}, '%')",
        "  </if>",
        "  <if test='consumptionId != null'>",
        "    AND a.consumption_id = #{consumptionId}",
        "  </if>",
        "  <if test='beginTime != null'>",
        "    AND a.record_time &gt;= #{beginTime}",
        "  </if>",
        "  <if test='endTime != null'>",
        "    AND a.record_time &lt;= #{endTime}",
        "  </if>",
        "</script>"
    })
    List<QueryBillsDTO> selectBillDetails(BillManageParam param);

    @Select({
        "<script>",
        "SELECT SUM(money) money",
        "FROM bill",
        "WHERE is_delete = 0",
        "  AND house_id = #{houseId}",
        "  AND bill_type = #{billType}",
        "  <if test='userId != null'>",
        "    AND user_id = #{userId}",
        "  </if>",
        "  <if test='recordId != null'>",
        "    AND user_id = #{recordId}",
        "  </if>",
        "  <if test='recordDate != null'>",
        "    AND record_time &gt;= #{recordDate}",
        "  </if>",
        "  <if test='beginTime != null'>",
        "    AND record_time &gt;= #{beginTime}",
        "  </if>",
        "  <if test='endTime != null'>",
        "    AND record_time &lt;= #{endTime}",
        "  </if>",
        "</script>"
    })
    Float selectBillTotalMoney(BillManageParam param);

    @Select({
        "<script>",
        "SELECT",
        "  b.name name,",
        "  SUM(a.money) money",
        "FROM bill a",
        "LEFT JOIN consumption_type b ON a.consumption_id = b.id",
        "WHERE a.is_delete = 0",
        "  AND a.house_id = #{houseId}",
        "  AND a.bill_type = 1",
        "  <if test='userId != null'>",
        "    AND a.user_id = #{userId}",
        "  </if>",
        "  <if test='recordId != null'>",
        "    AND a.user_id = #{recordId}",
        "  </if>",
        "  <if test='recordTime != null'>",
        "    AND a.record_time &gt;= #{recordTime}",
        "  </if>",
        "  <if test='beginTime != null'>",
        "    AND a.record_time &gt;= #{beginTime}",
        "  </if>",
        "  <if test='endTime != null'>",
        "    AND a.record_time &lt;= #{endTime}",
        "  </if>",
        "GROUP BY a.consumption_id",
        "</script>"
    })
    List<StatisticsByTypeDTO> selectBillTotalMoneyByType(BillManageParam param);

    @Insert({
        "<script>",
        "INSERT INTO bill",
        "<trim prefix='(' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>id,</if>",
        "  <if test='money != null'>money,</if>",
        "  <if test='consumptionId != null'>consumption_id,</if>",
        "  <if test='remark != null'>remark,</if>",
        "  <if test='recordTime != null'>record_time,</if>",
        "  <if test='userId != null'>user_id,</if>",
        "  <if test='houseId != null'>house_id,</if>",
        "  <if test='billType != null'>bill_type,</if>",
        "  <if test='createTime != null'>create_time,</if>",
        "  <if test='updateTime != null'>update_time,</if>",
        "  <if test='isDelete != null'>is_delete,</if>",
        "</trim>",
        "<trim prefix='VALUES (' suffix=')' suffixOverrides=','>",
        "  <if test='id != null'>#{id,jdbcType=INTEGER},</if>",
        "  <if test='money != null'>#{money,jdbcType=REAL},</if>",
        "  <if test='consumptionId != null'>#{consumptionId,jdbcType=INTEGER},</if>",
        "  <if test='remark != null'>#{remark,jdbcType=VARCHAR},</if>",
        "  <if test='recordTime != null'>#{recordTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='userId != null'>#{userId,jdbcType=INTEGER},</if>",
        "  <if test='houseId != null'>#{houseId,jdbcType=INTEGER},</if>",
        "  <if test='billType != null'>#{billType,jdbcType=INTEGER},</if>",
        "  <if test='createTime != null'>#{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>#{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='isDelete != null'>#{isDelete,jdbcType=INTEGER},</if>",
        "</trim>",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(Bill record);

    @Insert("INSERT INTO bill(money, consumption_id, remark, record_time, user_id, house_id, " +
            "bill_type, create_time, update_time, is_delete) " +
            "VALUES(#{money}, #{consumptionId}, #{remark}, #{recordTime}, #{userId}, #{houseId}, " +
            "#{billType}, #{createTime}, #{updateTime}, #{isDelete})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Bill record);

    @Delete("DELETE FROM bill WHERE id = #{id}")
    int deleteByPrimaryKey(Integer id);

    @Update("UPDATE bill SET " +
            "money = #{money}, " +
            "consumption_id = #{consumptionId}, " +
            "remark = #{remark}, " +
            "record_time = #{recordTime}, " +
            "user_id = #{userId}, " +
            "house_id = #{houseId}, " +
            "bill_type = #{billType}, " +
            "update_time = #{updateTime}, " +
            "is_delete = #{isDelete} " +
            "WHERE id = #{id}")
    int updateByPrimaryKey(Bill record);

    @Update({
        "<script>",
        "UPDATE bill",
        "<set>",
        "  <if test='money != null'>money = #{money},</if>",
        "  <if test='consumptionId != null'>consumption_id = #{consumptionId},</if>",
        "  <if test='remark != null'>remark = #{remark},</if>",
        "  <if test='recordTime != null'>record_time = #{recordTime},</if>",
        "  <if test='userId != null'>user_id = #{userId},</if>",
        "  <if test='houseId != null'>house_id = #{houseId},</if>",
        "  <if test='billType != null'>bill_type = #{billType},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete},</if>",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int updateByPrimaryKeySelective(Bill record);
}