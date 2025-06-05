<template>
  <div class="consume-manage">
    <div class="consume-manage-top">
      <el-button type="primary" @click="dialogFormVisible = true"
        >新增消费类型</el-button
      >
      <el-dialog
        title="新增消费类型"
        :visible.sync="dialogFormVisible"
        destroy-on-close
      >
        <el-form :model="form" :rules="rules" ref="form">
          <el-form-item
            label="消费类型名称"
            :label-width="formLabelWidth"
            prop="name"
          >
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addConsume('form')"
            >确 定</el-button
          >
        </div>
      </el-dialog>
    </div>
    <div class="consume-manage-content" v-loading="loading">
      <el-table
        class="user-manage-table"
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="index" label="序号" type="index" width="180">
        </el-table-column>
        <el-table-column prop="name" label="类型名称" width="180">
        </el-table-column>
        <el-table-column
          prop="createTime"
          :formatter="formatterTime"
          label="创建时间"
        >
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              slot="reference"
              @click="editConsumption(scope.row)"
              >编辑</el-button
            >
            <el-popconfirm
              v-if="scope.row.isCustom !== 0"
              :title="`确定将${scope.row.name}类型删除吗?`"
              @confirm="deleteRow(scope.row)"
            >
              <el-button
                type="text"
                size="small"
                slot="reference"
                style="margin-left:20px"
                >删除</el-button
              >
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { get, put, post } from '../../utils/http'
import moment from 'moment'
import { Message } from 'element-ui'

export default {
  name: 'ConsumeManage',
  mounted() {
    this.getList()
  },
  data() {
    return {
      dialogFormVisible: false,
      rules: {
        name: [
          {
            required: true,
            message: '消费类型名称不能为空',
            trigger: 'change',
          },
        ],
      },
      form: {
        name: '',
      },
      formLabelWidth: '120px',
      loading: false,
      params: {
        houseId: sessionStorage.getItem('houseId'),
      },
      tableData: [],
      modalTitle: '新增消费类型',
      selected: '',
    }
  },
  methods: {
    formatterTime(row, column, cellValue) {
      return moment(cellValue).format('YYYY-MM-DD HH:mm:ss')
    },
    tableRowClassName({ rowIndex }) {
      if (rowIndex === 1) {
        return 'warning-row'
      } else if (rowIndex === 3) {
        return 'success-row'
      }
      return ''
    },
    getList() {
      this.loading = true
      get('/consumptionType/query', {
        ...this.params,
      }).then((res) => {
        this.loading = false
        if (res.description === 'success') {
          this.tableData = res.data
        }
      })
    },
    addConsume(form) {
      this.$refs[form].validate((valid) => {
        if (valid) {
          let params = {
            name: this.form.name,
            houseId: sessionStorage.getItem('houseId'),
          }
          if (this.modalTitle === '编辑消费类型') {
            params.id = this.selected
            params.newName = this.form.name
            delete params.name
            put('/consumptionType/updateName', params).then((res) => {
              if (res.description === 'success') {
                Message.success({ message: '编辑消费类型成功' })
                this.dialogFormVisible = false
                this.getList()
              }
            })
          } else {
            post('/consumptionType/add', params).then((res) => {
              if (res.description === 'success') {
                Message.success({ message: '新增消费类型成功' })
                this.dialogFormVisible = false
                this.getList()
              }
            })
          }
        } else {
          return false
        }
      })
    },
    deleteRow(row) {
      put('/consumptionType/deleteType', { id: row.id }).then((res) => {
        if (res.description === 'success') {
          Message.success({ message: '删除消费类型成功' })

          this.getList()
        }
      })
    },
    editConsumption(row) {
      this.modalTitle = '编辑消费类型'
      this.dialogFormVisible = true
      this.form.name = row.name
      this.selected = row.id
    },
  },
}
</script>

<style>
.consume-manage {
  height: calc(100% - 30px);
}
.consume-manage-top {
  display: flex;
  justify-content: flex-end;
  padding-bottom: 15px;
}
.consume-manage-content {
  height: calc(100% - 55px);
}
</style>
