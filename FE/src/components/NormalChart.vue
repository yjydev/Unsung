<template>
  <div v-if="searchword">
  <v-chip
      label
      color="grey darken-3"
      background-color="white"
      text-color="white"
      small
      @click.stop=""
      >{{searchword}} 에 대한 언론사별 관심도
    </v-chip>
    <div v-if="normal_data['keyword'] == searchword">
    <!--<apexchart type="polarArea" :options="chartOptions" :series="series" width="500"></apexchart>-->
    <apexchart type="bar" height="430" :options="chartOptions2" :series="series2"></apexchart>
    <!--<apexchart type="bar" height="350" :options="chartOptions3" :series="series3"></apexchart>-->
    </div>
    <div v-else>
    <div style="vertical-align:center">
      <h1>{{searchword}} 에 대한 통계가 존재하지 않습니다.</h1>
    </div>
    </div>
  </div>
</template>
<script>
import http from "@/util/http-common";
import "@/css/font.css";
import {mapState} from 'vuex';
import ApexCharts from 'apexcharts'
import VueApexCharts from 'vue-apexcharts'

export default {
  name: "DoughnutChartItem",
  components:{
    apexchart: VueApexCharts,
  },
  data(){
    return{
      series2: [{
            data: []
          }, {
            data: [41280,99480,112570]
          }],
          chartOptions2: {
            chart: {
              type: 'bar',
              height: 430
            },
            labels:['기사 수', '전체 기사 수'],
            plotOptions: {
              bar: {
                horizontal: true,
                dataLabels: {
                  position: 'top',
                },
              }
            },
            dataLabels: {
              enabled: true,
              offsetX: -6,
              style: {
                fontSize: '12px',
                colors: ['#fff']
              }
            },
            stroke: {
              show: true,
              width: 1,
              colors: ['#fff']
            },
            tooltip: {
              shared: true,
              intersect: false
            },
            xaxis: {
              categories: ['JTBC', 'SBS', '중앙일보'],
            },
          },
      // series: [],
      //     chartOptions: {
      //       chart: {
      //         type: 'polarArea',
      //       },
      //       labels: ['JTBC', 'SBS', '중앙일보'],
      //       stroke: {
      //         colors: ['#fff']
      //       },
      //       fill: {
      //         opacity: 0.9
      //       },
      //       responsive: [{
      //         breakpoint: 750,
      //         options: {
      //           chart: {
      //             width: 300
      //           },
      //           legend: {
      //             position: 'bottom'
      //           }
      //         }
      //       }]
      //     },

      // series3: [{
      //       data: []
      //     }],
      //     chartOptions3: {
      //       chart: {
      //         type: 'bar',
      //         height: 350
      //       },
      //       plotOptions: {
      //         bar: {
      //           borderRadius: 4,
      //           horizontal: true,
      //         }
      //       },
      //       dataLabels: {
      //         enabled: false
      //       },
      //       xaxis: {
      //         categories: ['jtbc','sbs','middle'],
      //       }
      //     },
    }
  },
  methods:{
    updateChart(){
      if (this.searchword) {
        // this.series = []
        // this.$store.dispatch('GetChat', this.searchword)
        // this.series.push(this.normal_data['jtbc'])
        // this.series.push(this.normal_data['sbs'])
        // this.series.push(this.normal_data['middle'])

        this.series2[0]['data'] = []
        this.series2[0]['data'].push(this.normal_data['jtbc'])
        this.series2[0]['data'].push(this.normal_data['sbs'])
        this.series2[0]['data'].push(this.normal_data['middle'])

        // this.series3[0]['data'] = []
        // this.series3[0]['data'].push((this.normal_data['jtbc']/41280 * 100).toFixed(5))
        // this.series3[0]['data'].push((this.normal_data['sbs']/99480 * 100).toFixed(5))
        // this.series3[0]['data'].push((this.normal_data['middle']/112570 * 100).toFixed(5))
      }
    }
  },

  computed: {
    ...mapState([
      'searchword',
      'normal_data',
    ]),
    },
  watch:{
    'searchword':'updateChart'
  },
  mounted(){
    this.updateChart()
  },
  // updated(){
  //   this.updateChart()
  // }
};
</script>
<style>
.inline{
  display: inline-block;
}

</style>
