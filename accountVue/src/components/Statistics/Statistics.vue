<template>
  <div class="statistics">
    <div class="statistics-header">
      <div class="cards-container">
        <div class="stat-card income">
          <div class="stat-content">
            <div class="stat-label">收入</div>
            <div class="stat-value">{{ allData.totalIncome }}元</div>
          </div>
        </div>
        <div class="stat-card expense">
          <div class="stat-content">
            <div class="stat-label">支出</div>
            <div class="stat-value">{{ allData.totalExpend }}元</div>
          </div>
        </div>
        <div class="stat-card balance">
          <div class="stat-content">
            <div class="stat-label">余额</div>
            <div class="stat-value">{{ allData.totalRemain }}元</div>
          </div>
        </div>
      </div>
      <div class="filter-options">
        <el-select
          v-model="userId"
          placeholder="请选择家庭成员"
          @change="changeUserId"
          class="member-select"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
        <el-radio-group v-model="timeType" @change="changeTime" class="time-filter">
          <el-radio-button label="1">本月</el-radio-button>
          <el-radio-button label="2">近三月</el-radio-button>
          <el-radio-button label="3">近半年</el-radio-button>
          <el-radio-button label="4">近一年</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <div class="charts-container">
      <div class="chart-wrapper">
        <div id="lineChart" class="chart"></div>
      </div>
      <div class="chart-wrapper">
        <div id="pieChart" class="chart"></div>
    </div>
    </div>
  </div>
</template>

<script>
import { get } from '../../utils/http'

export default {
  name: 'Statistics',
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
      updateTimer: null,
    }
  },
  mounted() {
    this.getUser()
    this.changeTime()
    this.getPieData()
    
    this.updateTimer = setInterval(() => {
      this.getUser()
    }, 3000)
  },
  beforeDestroy() {
    if (this.updateTimer) {
      clearInterval(this.updateTimer)
    }
  },
  methods: {
    drawLine() {
      let myChart = this.$echarts.init(document.getElementById('lineChart'))
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
              if (element.money > 0) {
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
          const currentUserId = this.userId
          
          this.userList = res.data.list
          this.userList.unshift({
            id: '000000',
            name: '家庭总资产',
          })

          if (currentUserId && currentUserId !== '000000') {
            const userExists = this.userList.some(user => user.id === currentUserId)
            if (!userExists) {
              this.userId = '000000'
              this.changeTime()
            }
          }
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
.statistics {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  height: 100%;
}

.statistics-header {
  margin-bottom: 30px;
}

.cards-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.income {
  background: linear-gradient(135deg, #88d498 0%, #5aaa6f 100%);
}

.expense {
  background: linear-gradient(135deg, #ff9a9e 0%, #fad0c4 100%);
}

.balance {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.stat-content {
  color: white;
  text-align: center;
}

.stat-label {
  font-size: 16px;
  margin-bottom: 10px;
  opacity: 0.9;
}

.stat-value {
  font-size: 24px;
  font-weight: 500;
}

.filter-options {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.member-select {
  width: 200px;
}

.time-filter {
  .el-radio-button__inner {
    padding: 10px 20px;
    border: none;
    background: #f5f7fa;
    color: #606266;
  }

  .el-radio-button__orig-radio:checked + .el-radio-button__inner {
    background: #409EFF;
    color: white;
    box-shadow: none;
}
}

.charts-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 20px;
}

.chart-wrapper {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.chart {
  width: 100%;
  height: 400px;
}

@media screen and (max-width: 1200px) {
  .charts-container {
    grid-template-columns: 1fr;
  }
}

@media screen and (max-width: 768px) {
  .cards-container {
    grid-template-columns: 1fr;
  }

  .filter-options {
    flex-direction: column;
    align-items: stretch;
  }

  .member-select {
    width: 100%;
  }

  .time-filter {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }

  .chart {
    height: 300px;
  }
}
</style>
