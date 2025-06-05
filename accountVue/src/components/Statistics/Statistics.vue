<template>
  <div>
    <div class="top">
      <div class="card">
        <div class="card-item">
          <div>
            收入<span>{{ allData.totalIncome }}</span
            >元
          </div>
        </div>
        <div class="card-item">
          <div>
            支出<span>{{ allData.totalExpend }}</span
            >元
          </div>
        </div>
        <div class="card-item">
          <div>
            余额<span>{{ allData.totalRemain }}</span
            >元
          </div>
        </div>
        <!-- <div class="card-item">
          <div>预算<span>{{ allData.totalBudget }}</span>元</div>
        </div> -->
      </div>
      <div class="top-options">
        <el-select
          style="width: 290px; margin-right: 20px"
          v-model="userId"
          placeholder="请选择家庭成员"
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
        <el-radio-group v-model="timeType" @change="changeTime">
          <el-radio-button label="1">本月</el-radio-button>
          <el-radio-button label="2">近三月</el-radio-button>
          <el-radio-button label="3">近半年</el-radio-button>
          <el-radio-button label="4">近一年</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <div style="display: flex">
      <div id="lineChart"></div>
    </div>
    <div style="display: flex">
      <div id="pieChart"></div>
    </div>
  </div>
</template>

<script>
import { get } from '../../utils/http'
export default {
  name: 'Statistics',
  mounted() {
    this.getUser()
    this.changeTime()
    this.getPieData()
    console.log(this.pieData)
  },
  data() {
    return {
      timeType: '1',
      allData: {},
      lineData: {},
      pieData: [],
      userList: [
        {
          id: '000000',
          name: '家庭总资产',
        }
      ],
      userId: '000000',
      onlySelf: false,
    }
  },
  methods: {
    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById('lineChart'))
      // 绘制图表
      myChart.setOption({
        title: {
          text: '收入支出统计',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['收入', '支出'],
        },
        color: ['#CCCCFF', '#FFCCCC'],
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.lineData.dateList ? this.lineData.dateList : [],
          axisLine: {
            show: false,
          },
        },
        yAxis: {
          type: 'value',
          splitLine: {
            lineStyle: {
              type: 'dashed',
            },
          },
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
        },
        series: [
          {
            name: '收入',
            type: 'line',
            stack: '总量1',
            data: this.lineData.incomeList ? this.lineData.incomeList : [],
          },
          {
            name: '支出',
            type: 'line',
            stack: '总量2',
            data: this.lineData.expendList ? this.lineData.expendList : [],
          },
        ],
      })
    },
    drawPie() {
      // 基于准备好的dom，初始化echarts实例
      let myChart = this.$echarts.init(document.getElementById('pieChart'))
      
      const option = {
        title: {
          text: '消费类型',
          subtext: this.pieData.length === 0 ? '暂无消费数据' : '',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: this.pieData.map((item) => item.name)
        },
        color: [
          '#85c7de',
          '#e7d8c9',
          '#CDB4DB',
          '#c9f2c7',
          '#FFC8DD',
          '#FFAFCC',
          '#BDE0FE',
          '#A2D2FF',
          '#FEC5BB',
          '#D8E2DC',
          '#B0C4B1',
          '#EAAC8B',
          '#88d498',
        ],
        series: [
          {
            name: '消费金额',
            type: 'pie',
            radius: '65%',
            center: ['50%', '60%'],
            data: this.pieData,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            label: {
              show: true,
              formatter: '{b}: {c}元 ({d}%)'
            }
          }
        ]
      }
      myChart.setOption(option)
    },
    getData() {
      //
      console.log("数据", this.userId)
      let userId = ""

      if(this.userId === "000000"){
        userId = "-1"
      }else {
        userId = this.userId
      }
      console.log("数据", userId)
      get('/bill/statisticsByTime', {
        houseId: sessionStorage.getItem('houseId'),
        userId: userId,
        timeType: this.timeType,
        onlySelf: this.onlySelf,
      }).then((res) => {
        if (res.description === 'success') {
          this.allData = {
            totalIncome: res.data.totalIncome,
            totalExpend: res.data.totalExpend,
            totalBudget: res.data.totalBudget,
            totalRemain: (res.data.totalIncome - res.data.totalExpend).toFixed(
              2
            ),
          }
          console.log(this.allData.totalIncome)
          //this.drawPie()
        }
      })
    },
    getLineData() {
      let userId = ""

      if(this.userId === "000000"){
        userId = "-1"
      }else {
        userId = this.userId
      }
      get('/bill/statisticsLineChart', {
        houseId: sessionStorage.getItem('houseId'),
        userId:userId,
        timeType: this.timeType,
        onlySelf: this.onlySelf,
      }).then((res) => {
        if (res.description === 'success') {
          Object.assign(this.lineData, res.data)
          console.log(this.lineData);
          this.drawLine()
        }
      })
    },
    getPieData() {
      let userId = ""

      if(this.userId === "000000"){
        userId = "-1"
      }else {
        userId = this.userId
      }
      get('/bill/statisticsType', {
        houseId: sessionStorage.getItem('houseId'),
        userId:userId,
        timeType: this.timeType,
        onlySelf: this.onlySelf,
      }).then((res) => {
        if (res.description === 'success') {
          let newData = []
          if (res.data && res.data.length > 0) {
            res.data.forEach((element) => {
              if (element.money > 0) {  // 只添加金额大于0的数据
                newData.push({ name: element.name, value: element.money })
              }
            })
          }
          this.pieData = newData
          this.drawPie()
        }
      })
    },
    changeTime() {
      this.getData()
      this.getLineData()
      this.getPieData()
    },
    getUser() {
      get('/user/query', {
        houseId: sessionStorage.getItem('houseId'),
      }).then((res) => {
        if (res.description === 'success') {
          this.userList = res.data.list
          this.userList.unshift({
            id: '000000',
            name: '家庭总资产',
          })
        }
      })
    },
    changeUserId(userId) {
      this.onlySelf = false
      if (userId === Number(sessionStorage.getItem('userId'))) {
        this.onlySelf = true
      }
      this.userId = userId

      this.changeTime()
    },
  },
}
</script>

<style>
.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.top-options {
  display: flex;
  align-items: flex-end;
}
.card {
  display: flex;
  padding-bottom: 20px;
}
.card-item {
  display: flex;
  flex-direction: column;
  padding: 20px 30px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  border-radius: 6px;
  margin-right: 20px;
}

.card-item:nth-child(1) span {
  padding: 0 5px;
  color: #67c23a;
}
.card-item:nth-child(2) span {
  color: #e6a23c;
  padding: 0 5px;
}
.card-item:nth-child(3) span {
  color: #f56c6c;
  padding: 0 5px;
}
.card-item:nth-child(4) span {
  color: #909399;
  padding: 0 5px;
}
#lineChart {
  width: calc(100% - 40px);
  padding: 20px 20px;
  height: 600px;
}
#pieChart {
  width: calc(50% - 40px);
  padding: 20px 20px;
  height: 400px;
}
</style>
