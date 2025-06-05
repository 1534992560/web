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


    @Select({
        "<script>",
        "SELECT",
        "  a.id id,",
        "  a.money money,",
        "  a.consumption_id consumptionId,",
        "  b.name consumptionName,",
        "  b.is_custom isCustom,",
        "  a.income_id incomeId,",
        "  c.name incomeName,",
        "  c.is_custom incomeIsCustom,",
        "  a.remark remark,",
        "  a.record_time recordTime,",
        "  a.user_id userId,",
        "  d.name userName,",
        "  a.house_id houseId,",
        "  a.bill_type billType",
        "FROM bill a",
        "LEFT JOIN consumption_type b ON a.consumption_id = b.id AND b.is_delete = 0",
        "LEFT JOIN income_type c ON a.income_id = c.id AND c.is_delete = 0",
        "LEFT JOIN user d ON d.id = a.user_id",
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
        "  <if test='incomeId != null'>",
        "    AND a.income_id = #{incomeId}",
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
        "  name,",
        "  SUM(money) money",
        "FROM (",
        "  SELECT",
        "    CASE WHEN #{billType} = 1 THEN b.name ELSE c.name END as name,",
        "    a.money,",
        "    CASE WHEN #{billType} = 1 THEN a.consumption_id ELSE a.income_id END as type_id",
        "  FROM bill a",
        "  LEFT JOIN consumption_type b ON a.consumption_id = b.id AND b.is_delete = 0",
        "  LEFT JOIN income_type c ON a.income_id = c.id AND c.is_delete = 0",
        "  WHERE a.is_delete = 0",
        "    AND a.house_id = #{houseId}",
        "    AND a.bill_type = #{billType}",
        "    <if test='userId != null'>",
        "      AND a.user_id = #{userId}",
        "    </if>",
        "    <if test='recordId != null'>",
        "      AND a.user_id = #{recordId}",
        "    </if>",
        "    <if test='recordTime != null'>",
        "      AND a.record_time &gt;= #{recordTime}",
        "    </if>",
        "    <if test='beginTime != null'>",
        "      AND a.record_time &gt;= #{beginTime}",
        "    </if>",
        "    <if test='endTime != null'>",
        "      AND a.record_time &lt;= #{endTime}",
        "    </if>",
        ") t",
        "GROUP BY name",
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
        "  <if test='incomeId != null'>income_id,</if>",
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
        "  <if test='incomeId != null'>#{incomeId,jdbcType=INTEGER},</if>",
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


    @Update({
        "<script>",
        "UPDATE bill",
        "<set>",
        "  <if test='money != null'>money = #{money,jdbcType=REAL},</if>",
        "  <if test='consumptionId != null'>consumption_id = #{consumptionId,jdbcType=INTEGER},</if>",
        "  <if test='incomeId != null'>income_id = #{incomeId,jdbcType=INTEGER},</if>",
        "  <if test='remark != null'>remark = #{remark,jdbcType=VARCHAR},</if>",
        "  <if test='recordTime != null'>record_time = #{recordTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='userId != null'>user_id = #{userId,jdbcType=INTEGER},</if>",
        "  <if test='houseId != null'>house_id = #{houseId,jdbcType=INTEGER},</if>",
        "  <if test='billType != null'>bill_type = #{billType,jdbcType=INTEGER},</if>",
        "  <if test='createTime != null'>create_time = #{createTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='updateTime != null'>update_time = #{updateTime,jdbcType=TIMESTAMP},</if>",
        "  <if test='isDelete != null'>is_delete = #{isDelete,jdbcType=INTEGER},</if>",
        "</set>",
        "WHERE id = #{id,jdbcType=INTEGER}",
        "</script>"
    })
    int updateByPrimaryKeySelective(Bill record);
}