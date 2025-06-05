<template>
  <div class="consume-manage">
    <div class="consume-manage-header">
      <h2 class="page-title">消费类型管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="dialogFormVisible = true" class="add-button">
        新增消费类型
      </el-button>
    </div>

    <div class="consume-manage-content" v-loading="loading">
      <el-table
        class="consume-table"
        :data="tableData"
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="index" label="序号" type="index" width="80" align="center">
        </el-table-column>
        <el-table-column prop="name" label="类型名称" min-width="150" align="center">
        </el-table-column>
        <el-table-column
          prop="createTime"
          :formatter="formatterTime"
          label="创建时间"
          min-width="180"
          align="center"
        >
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button
                type="text"
                size="small"
                class="action-button edit"
                @click="editConsumption(scope.row)"
              >编辑</el-button>
              <el-divider direction="vertical"></el-divider>
              <el-popconfirm
                v-if="scope.row.isCustom !== 0"
                :title="`确定将${scope.row.name}类型删除吗?`"
                @confirm="deleteRow(scope.row)"
                confirm-button-text="确定"
                cancel-button-text="取消"
                icon="el-icon-warning"
                icon-color="#ff4949"
              >
                <el-button
                  type="text"
                  size="small"
                  slot="reference"
                  class="action-button delete"
                >删除</el-button>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      :title="modalTitle"
      :visible.sync="dialogFormVisible"
      width="400px"
      destroy-on-close
      custom-class="consume-dialog"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="消费类型名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入消费类型名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addConsume('form')">确 定</el-button>
      </div>
    </el-dialog>
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
  height: 100%;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.consume-manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 20px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

.add-button {
  padding: 12px 20px;
  font-weight: 500;
  border-radius: 4px;
  transition: all 0.3s;
}

.add-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.consume-manage-content {
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.consume-table {
  margin-bottom: 20px;
}

/* 表格样式 */
.el-table {
  border-radius: 8px;
}

.el-table th {
  background-color: #f5f7fa !important;
  color: #606266;
  font-weight: 500;
  height: 50px;
}

.el-table td {
  padding: 12px 0;
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-button {
  padding: 4px 8px;
  font-size: 13px;
  transition: all 0.3s;
}

.action-button.edit {
  color: #409EFF;
}

.action-button.delete {
  color: #F56C6C;
}

.action-button:hover {
  opacity: 0.8;
  transform: translateY(-1px);
}

/* 弹窗样式 */
.consume-dialog {
  border-radius: 8px;
  overflow: hidden;
}

.el-dialog__header {
  background: #f8f9fb;
  padding: 20px;
  margin: 0;
}

.el-dialog__title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.el-dialog__body {
  padding: 30px 20px;
}

.el-form-item {
  margin-bottom: 22px;
}

.el-form-item__label {
  font-weight: normal;
  color: #606266;
}

.el-input__inner {
  height: 40px;
  line-height: 40px;
  border-radius: 4px;
}

.dialog-footer {
  padding: 20px;
  border-top: 1px solid #ebeef5;
  text-align: right;
}

.dialog-footer .el-button {
  padding: 9px 20px;
}

/* Popconfirm 样式 */
.el-popconfirm__main {
  padding: 16px 0;
  font-size: 14px;
  color: #606266;
}

.el-popconfirm__action {
  margin-top: 16px;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .consume-manage {
    padding: 15px;
  }

  .consume-manage-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }

  .add-button {
    width: 100%;
  }

  .action-buttons {
    flex-direction: column;
    gap: 8px;
  }

  .el-divider--vertical {
    display: none;
  }

  .action-button {
    width: 100%;
    text-align: center;
  }

  .el-dialog {
    width: 90% !important;
    margin: 0 auto;
  }

  .el-form-item__label {
    float: none;
    text-align: left;
    margin-bottom: 8px;
  }

  .el-form-item__content {
    margin-left: 0 !important;
  }
}
</style>
