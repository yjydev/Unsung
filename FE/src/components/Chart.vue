<template>
  <v-row>
  <v-col
    cols="5"
    md="5"
  >
  <v-chip
      label
      color="grey darken-3"
      background-color="white"
      text-color="white"
      small
      @click.stop=""
      >{{searchword}} 의 언론사별 성향 그래프
    </v-chip>
    <div>
       <div id="chart" class="inline">
        <apexchart type="donut" :options="chartOptions" :series="series_JTBC"></apexchart>
        <h4 class="center">언론사 : JTBC</h4>
      </div>
      <div id="chart" class="inline">
        <apexchart type="donut" :options="chartOptions" :series="series_SBS"></apexchart>
        <h4 class="center">언론사 : SBS</h4>
      </div>
    </div>
  </v-col>
  <v-col
    cols="5"
    md="5"
  >
  <v-chip
      label
      color="transparent"
      background-color="white"
      text-color="white"
      small
      @click.stop=""
      >
    </v-chip>
  <div>
      <div id="chart" class="inline">
        <apexchart type="donut" :options="chartOptions" :series="series_MIDDLE"></apexchart>
        <h4 class="center">언론사 : 중앙일보</h4>
      </div>
      <div id="chart" class="inline">
        <apexchart type="donut" :options="chartOptions" :series="series_KBS"></apexchart>
        <h4 class="center">언론사 : KBS</h4>
      </div>
    </div>
  </v-col>
  </v-row>
</template>
<script>
import http from "@/util/http-common";
import "@/css/font.css";
import {mapState} from 'vuex';
import VueApexCharts from 'vue-apexcharts'
export default {
  name: "DoughnutChartItem",
  components:{
    apexchart: VueApexCharts,
  },
  data(){
    return{
      series_JTBC: [44, 55, 41, 17],
      series_SBS: [44, 55, 41, 17],
      series_MIDDLE: [44, 55, 41, 17],
      series_KBS: [44, 55, 41, 17],
          chartOptions: {
            chart: {
              type: 'donut',
            },
            labels:["긍정", "부정", "중립", "미분류"],
            responsive: [{
              breakpoint: 480,
              options: {
                chart: {
                  width: 400
                },
                legend: {
                  position: 'bottom'
                }
              }
            }]
          },

    }
  },
  methods:{
    updateChart(){
      if(this.searchword){
        this.series_JTBC = []
        this.series_JTBC[0] = this.doughnut_data_JTBC['positive']
        this.series_JTBC[1] = this.doughnut_data_JTBC['negative']
        this.series_JTBC[2] = this.doughnut_data_JTBC['neutral']
        this.series_JTBC[3] = this.doughnut_data_JTBC['unclassified']

        this.series_SBS = []
        this.series_SBS[0] = this.doughnut_data_SBS['positive']
        this.series_SBS[1] = this.doughnut_data_SBS['negative']
        this.series_SBS[2] = this.doughnut_data_SBS['neutral']
        this.series_SBS[3] = this.doughnut_data_SBS['unclassified']

        this.series_MIDDLE = []
        this.series_MIDDLE[0] = this.doughnut_data_MIDDLE['positive']
        this.series_MIDDLE[1] = this.doughnut_data_MIDDLE['negative']
        this.series_MIDDLE[2] = this.doughnut_data_MIDDLE['neutral']
        this.series_MIDDLE[3] = this.doughnut_data_MIDDLE['unclassified']

        this.series_KBS = []
        this.series_KBS[0] = this.doughnut_data_KBS['positive']
        this.series_KBS[1] = this.doughnut_data_KBS['negative']
        this.series_KBS[2] = this.doughnut_data_KBS['neutral']
        this.series_KBS[3] = this.doughnut_data_KBS['unclassified']
      }
    }
  },
  computed: {
    ...mapState([
      'searchword',
      'doughnut_data_JTBC',
      'doughnut_data_SBS',
      'doughnut_data_MIDDLE',
      'doughnut_data_KBS'
    ]),
    },
    watch:{
      'searchword' : 'updateChart'
    },
    mounted(){
      this.updateChart()
    }

};
</script>
<style>
.inline{
  display: inline-block;
}
</style>
