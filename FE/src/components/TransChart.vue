<template>
  <div v-if="searchword">
  <v-row>
  <v-chip
      label
      color="grey darken-3"
      background-color="white"
      text-color="white"
      small
      @click.stop=""
      >{{searchword}} 에 대한 언론사별 성향 변화 추이
  </v-chip>
  </v-row>

  <v-row>
    <v-col align="left" cols="1">
    <v-select :items="items" label="언론사 선택" filled v-model="press" @change="get_trans"></v-select>
    </v-col>
  </v-row>

  <v-row>
    <v-col cols="12">
    <div v-if="trans_data['keyword'] == searchword">
     <apexchart type="line" height="550" width="100%" :options="chartOptions" :series="series"></apexchart>
    </div>

    <div v-else>
    <div style="vertical-align:center">
      <h1>{{searchword}} 에 대한 통계가 존재하지 않습니다.</h1>
    </div>
    </div>
    </v-col>
    </v-row>
  </div>
</template>
<script>
import http from "@/util/http-common";
import "@/css/font.css";
import {mapState} from 'vuex';
import ApexCharts from 'apexcharts'
import VueApexCharts from 'vue-apexcharts'

export default {
  name: "TransChart",
  components:{
    apexchart: VueApexCharts,
  },
  data(){
    return{
      items:['jtbc','sbs','중앙일보'],
      press : '',
      series: [{
            name: '긍정',
            type: 'column',
            data: [],
            color: '#3498db',
          }, {
            name: '부정',
            type: 'column',
            data: [],
            color: '#e74c3c',
          }, {
            name: '전체',
            type: 'line',
            data: [],
            color :'#9575cd',
          }],
      chartOptions: {
            chart: {
              height: 500,
              type: 'line',
              stacked: false
            },
            dataLabels: {
              enabled: false
            },
            stroke: {
              width: [1, 1, 4]
            },
            title: {
              text: `${this.searchword} 에 대한 성향`,
              align: 'left',
              offsetX: 110
            },
            xaxis: {
              categories: ['2021-01','2021-02','2021-03','2021-04','2021-05','2021-06','2021-07','2021-08','2021-09','2021-10','2021-11','2021-12','2022-01','2022-02','2022-03'],
            },
            yaxis: [
              { seriesName: '긍정',
                axisTicks: {
                  show: true,
                },
                axisBorder: {
                  show: true,
                  color: '#3498db',
                },
                labels: {
                  style: {
                    colors: '#3498db',
                  }
                },
                title: {
                  text: "긍정적인 기사 (개)",
                  style: {
                    color: '#3498db',
                  }
                },
                tooltip: {
                  enabled: true
                }
              },
              {
                seriesName: '부정',
                opposite: true,
                axisTicks: {
                  show: true,
                },
                axisBorder: {
                  show: true,
                  color:'#e74c3c',
                },
                labels: {
                  style: {
                    colors: '#e74c3c',
                  }
                },
                title: {
                  text: "부정적인 기사 (개) ",
                  style: {
                    color: '#e74c3c',
                  }
                },
              },
              {
                seriesName: '전체 통계',
                opposite: true,
                axisTicks: {
                  show: true,
                },
                axisBorder: {
                  show: true,
                  color: '#9575cd',
                },
                labels: {
                  style: {
                    colors: '#9575cd',
                  },
                },
                title: {
                  text: "전체 기사 (개) ",
                  style: {
                    color: '#9575cd',
                  }
                }
              },
            ],
            tooltip: {
              fixed: {
                enabled: true,
                position: 'topLeft', // topRight, topLeft, bottomRight, bottomLeft
                offsetY: 30,
                offsetX: 60
              },
            },
            legend: {
              horizontalAlign: 'left',
              offsetX: 40
            },
          }
    }
  },
  methods:{
    get_trans(){
      this.$route.go;
      if (this.searchword) {
        // this.series = []
        this.series[0]['data'] = []
        this.series[1]['data'] = []
        this.series[2]['data'] = []
        this.$store.dispatch('GetTransChart', [this.searchword, this.press])
        this.chartOptions['xaxis']['categories'].forEach(date=>{
          // console.log(date in Object.keys(this.trans_data))
          if (Object.keys(this.trans_data).includes(date)) {
            var val = this.trans_data[date]
            this.series[0]['data'].push(val[0])
            this.series[1]['data'].push(val[1])
            this.series[2]['data'].push(val[2])
          } else {
            this.series[0]['data'].push(0)
            this.series[1]['data'].push(0)
            this.series[2]['data'].push(0)
          }
        })
        console.log(this.series)
        
      }
    },
    update_press() {
      this.$store.dispatch()
    }
  },

  computed: {
    ...mapState([
      'searchword',
      'trans_data',
      'trans_press',
    ]),
    },
  watch:{
    'searchword':'get_trans',
    'press':'get_trans',
    'press':'update_press',
  },
  mounted(){
    this.get_trans()
  },
};

</script>

<style>
.inline{
  display: inline-block;
}

</style>
