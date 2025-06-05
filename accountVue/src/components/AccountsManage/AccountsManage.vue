<template>
  <div class="accounts-manage">
    <div class="accounts-manage-top">
      <div class="accounts-manage-top-line">
        <div class="accounts-manage-top-item">
          <span class="search-name">关键字</span>
          <el-input
            style="width: 290px; margin-right: 20px"
            placeholder="请输入内容"
            v-model="keyWords"
            @keyup.enter.native="searchList"
          >
            <el-button
              @click="searchList"
              slot="append"
              icon="el-icon-search"
            ></el-button>
          </el-input>
        </div>
        <div class="accounts-manage-top-item">
          <span class="search-name">账单类型</span>
          <el-select
            style="width: 250px; margin-right: 20px"
            v-model="params.billType"
            placeholder="请选择"
            @change="changeBillType"
          >
            <el-option
              v-for="item in billTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div class="accounts-manage-top-item">
          <el-date-picker
            v-model="date"
            class="date"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="changeDate"
            value-format="yyyy-MM-dd HH:mm:ss"
          >
          </el-date-picker>
        </div>
      </div>
      <div class="accounts-manage-top-line">
        <div class="accounts-manage-top-item">
          <span class="search-name">家庭成员</span>
          <el-select
            style="width: 290px; margin-right: 20px"
            v-model="params.userId"
            placeholder="请选择"
            @change="changeUserId"
          >
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </div>
        <div
          class="accounts-manage-top-item"
          :style="this.params.billType === '1' ? 'display: none' : ''"
        >
          <span class="search-name">消费类型</span>
          <el-select
            style="width: 250px; margin-right: 20px"
            v-model="params.consumptionId"
            placeholder="请选择"
            @change="changeConsumptionId"
          >
            <el-option
              v-for="item in allConsumeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </div>

        <el-button
          type="primary"
          @click="
            accountModalVisible = true
            modalTitle = '新增账单'
          "
          >新增账单</el-button
        >
      </div>

      <el-dialog
        :title="modalTitle"
        :visible.sync="accountModalVisible"
        destroy-on-close
        @close="
          accountModalVisible = false
          form = {
            billType: '1',
            consumptionId: '',
            recordTime: '',
            money: '',
            remark: '',
          }
        "
      >
        <el-form :model="form" :rules="rules" ref="form">
          <el-form-item
            label="账单类型"
            :label-width="formLabelWidth"
            prop="billType"
          >
            <el-radio v-model="form.billType" label="1">支出</el-radio>
            <el-radio v-model="form.billType" label="2">收入</el-radio>
          </el-form-item>
          <el-form-item
            :style="form.billType === '2' ? 'display: none' : ''"
            label="消费类型"
            :label-width="formLabelWidth"
            prop="consumptionId"
          >
            <el-select
              style="width: 300px"
              v-model="form.consumptionId"
              placeholder="请选择"
            >
              <el-option
                v-for="item in consumeList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="金额" :label-width="formLabelWidth" prop="money">
            <el-input
              style="width: 300px"
              v-model="form.money"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="日期"
            :label-width="formLabelWidth"
            prop="recordTime"
          >
            <el-date-picker
              v-model="form.recordTime"
              type="datetime"
              placeholder="选择日期"
              style="width: 300px"
              :picker-消费类型="pickerOptions"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item
            label="备注"
            :label-width="formLabelWidth"
            prop="remark"
          >
            <el-input
              type="textarea"
              style="width: 300px"
              v-model="form.remark"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button
            @click="
              accountModalVisible = false
              form = {
                billType: '1',
                consumptionId: '',
                recordTime: new Date().toUTCString(),
                money: '',
                remark: '',
              }
            "
            >取 消</el-button
          >
          <el-button type="primary" @click="addAccount('form')"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </div>
    <div style="height: calc(100% - 80px)" v-loading="loading">
      <el-table
        class="accounts-manage-table"
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="index" label="序号" type="index">
        </el-table-column>
        <el-table-column prop="userName" label="记账人"> </el-table-column>

        <el-table-column label="账单类型">
          <template slot-scope="scope">
            {{ scope.row.billType === 2 ? '收入' : '支出' }}
          </template>
        </el-table-column>
        <el-table-column prop="consumptionName" label="消费类型">
        </el-table-column>
        <el-table-column prop="money" label="金额"> </el-table-column>
        <el-table-column
          prop="recordTime"
          :formatter="formatterTime"
          label="创建时间"
        >
        </el-table-column>
        <el-table-column prop="remark" label="备注"> </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button
              @click="editAccount(scope.row)"
              type="text"
              size="small"
              style="margin-right: 20px"
              >编辑</el-button
            >
            <el-popconfirm
              :title="`确定将此账单移除吗`"
              @confirm="deleteRow(scope.row)"
            >
              <el-button type="text" size="small" slot="reference"
                >删除</el-button
              >
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: right; padding: 10px 0 10px 10px">
        <el-pagination
          style="padding: 0"
          background
          layout="prev, pager, next"
          :current-page="pageNum"
          :total="total"
          :page-size="pageSize"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { get, post, put } from '../../utils/http'
import moment from 'moment'
import { Message } from 'element-ui'

export default {
  name: 'AccountsManage',
  data() {
    let validateMoney = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入金额'))
      } else {
        let reg = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/
        if (!reg.test(value)) {
          callback(new Error('金额为整数或最多两位小数'))
        }

        callback()
      }
    }
    return {
      accountModalVisible: false,
      tableData: [],
      billTypeOptions: [
        {
          value: '000000',
          label: '全部',
        },
        {
          value: '2',
          label: '收入',
        },
        {
          value: '1',
          label: '支出',
        },
      ],
      form: {
        billType: '1',
        consumptionId: '',
        recordTime: new Date().toUTCString(),
        money: '',
        remark: '',
      },
      rules: {
        billType: [
          { required: true, message: '消费类型不能为空', trigger: 'change' },
        ],
        money: [
          { required: true, message: '金额不能为空' },
          { validator: validateMoney, trigger: 'change' },
        ],
        consumptionId: [
          {
            required: this.billType === '1' ? true : false,
            message: '消费类型不能为空',
          },
        ],
        recordTime: [{ required: true, message: '日期不能为空' }],
      },
      loading: false,
      formLabelWidth: '120px',
      keyWords: '',
      allConsumeList: [],
      consumeList: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      params: {
        houseId: sessionStorage.getItem('houseId'),
        userId: '000000',
        consumptionId: '000000',
        billType: '000000',
        onlySelf: false,
      },
      date: [],
      modalTitle: '新增账单',
      selectId: '',
      userList: [],
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
      },
    }
  },
  mounted() {
    this.getList()
    this.getUser()
    this.getConsumeList()
  },
  methods: {
    formatterTime(row, column, cellValue) {
      return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
    },
    getConsumeList() {
      get('/consumptionType/query', {
        houseId: this.params.houseId,
      }).then((res) => {
        this.loading = false
        if (res.description === 'success') {
          this.consumeList = JSON.parse(JSON.stringify(res.data))
          this.allConsumeList = JSON.parse(JSON.stringify(res.data))
          this.allConsumeList.unshift({
            id: '000000',
            name: '全部',
          })
        }
      })
    },
    tableRowClassName({ rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row'
      } else if (rowIndex === 3) {
        return 'success-row'
      }
      return ''
    },
    addAccount(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let params = {
            userId: sessionStorage.getItem('userId'),
            houseId: sessionStorage.getItem('houseId'),
            consumptionId: this.form.consumptionId,
            billType: this.form.billType,
            money: this.form.money,
            remark: this.form.remark,
            recordTime: moment(this.form.recordTime).format(
              'YYYY-MM-DD HH:mm:ss'
            ),
          }
          if (this.form.billType === '2') {
            params.consumptionId = 0
          }
          if (this.modalTitle === '编辑账单') {
            params.id = this.selectId
            put('/bill/update', params).then((res) => {
              if (res.description === 'success') {
                Message.success({ message: '编辑账单成功' })
                this.accountModalVisible = false
                this.pageNum = 1
                this.getList()
                this.form = {
                  billType: '1',
                  consumptionId: '',
                  recordTime: '',
                  money: '',
                  remark: '',
                }
              }
            })
          } else {
            post('/bill/add', params).then((res) => {
              if (res.description === 'success') {
                Message.success({ message: '新增账单成功' })
                this.accountModalVisible = false
                this.pageNum = 1
                this.getList()
              }
            })
          }
        } else {
          return false
        }
      })
    },
    getList() {
      this.loading = true
      let { userId, consumptionId, billType } = this.params
      if (userId === '000000') {
        // userId = sessionStorage.getItem('userId')
        userId = "-1"
      }
      if (consumptionId === '000000') {
        consumptionId = ''
      }
      if (billType === '000000') {
        billType = ''
      }
      get('/bill/query', {
        ...this.params,
        userId: userId,
        consumptionId,
        billType,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        remark: this.keyWords,
      }).then((res) => {
        this.loading = false
        if (res.description === 'success') {
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },
    handleCurrentChange(val) {
      this.pageNum = val
      this.getList()
    },
    changeBillType(type) {
      this.pageNum = 1
      if (type !== '0') {
        this.params.billType = type
      } else {
        delete this.params.billType
      }
      this.getList()
    },
    changeConsumptionId(id) {
      this.pageNum = 1
      if (id !== '0') {
        this.params.consumptionId = id
      } else {
        delete this.params.consumptionId
      }
      this.getList()
    },
    changeDate(date) {
      this.pageNum = 1
      if (date !== null) {
        this.params.beginDate = date[0]
        this.params.endDate = date[1]
      } else {
        delete this.params.beginDate
        delete this.params.endDate
      }
      this.getList()
    },
    changeUserId(userId) {
      this.pageNum = 1
      this.params.onlySelf = false
      if (userId === Number(sessionStorage.getItem('userId'))) {
        this.params.onlySelf = true
      }
      this.params.userId = userId
      this.getList()
    },
    editAccount(row) {
      this.modalTitle = '编辑账单'
      this.accountModalVisible = true
      this.form.billType = row.billType + ''
      this.form.consumptionId = row.consumptionId
      this.form.money = row.money
      this.form.recordTime = row.recordTime
      this.form.remark = row.remark
      this.selectId = row.id
    },
    deleteRow(row) {
      put('/bill/remove', { id: row.id }).then((res) => {
        if (res.description === 'success') {
          Message.success({ message: '删除账单成功' })
          this.pageNum = 1
          this.getList()
        }
      })
    },
    searchList() {
      this.pageNum = 1
      this.getList()
    },
    getUser() {
      get('/user/query', {
        houseId: sessionStorage.getItem('houseId'),
      }).then((res) => {
        if (res.description === 'success') {
          this.userList = res.data.list
          this.userList.unshift({
            id: '000000',
            name: '全部',
          })
        }
      })
    },
  },
}
</script>

<style>
.accounts-manage-top-line {
  display: flex;
  padding: 0 0 20px 0;
}
.search-name {
  font-size: 14px;
  width: 56px;
}
.accounts-manage-top-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
}
.accounts-manage-top-item:nth-last-child(1) {
  margin-right: 0;
}
.accounts-manage-top-item span {
  margin-right: 10px;
}
.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}
.accounts-manage-table {
  border: 1px solid #ebebeb;
}
.date {
}
</style>
