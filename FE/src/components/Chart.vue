<template>
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
      >키워드의 언론사 성향 그래프
    </v-chip>
      <div class="center" id="my_dataviz">
      </div>
  </v-col>
</template>
<script>
import http from "@/util/http-common";
import "@/css/font.css";
import {mapState} from 'vuex';

export default {
  name: "DoughnutChartItem",
  // props: {
  //   data: {
  //     type: Array,
  //     required: true
  //   }
  // },
  // data(){
  //   return {
  //     a: 9, b: 20, c: 30, d: 8, e: 12
  //   }
  // },
  mounted() {
    this.drawChart();
  },
  methods:{
    drawChart(){
      const d3 = require("d3");
      // set the dimensions and margins of the graph
          var width = 450;
          var height = 450;
          var margin = 40;

          // The radius of the pieplot is half the width or half the height (smallest one). I subtract a bit of margin.
          var radius = Math.min(width, height) / 2 - margin;

          // append the svg object to the div called 'my_dataviz'
          var svg = d3
            .select('#my_dataviz')
            .append('svg')
            .attr('width', width)
            .attr('height', height)
            .append('g')
            .attr(
              'transform',
              'translate(' + width / 2 + ',' + height / 2 + ')'
            );
          // http.get(`/api/`)
          // Create dummy data
          var data = { a: 20, b: 20, c: 20, d: 20, e: 20 };

          // set the color scale
          var color = d3
            .scaleOrdinal()
            .domain(Object.keys(data))
            .range(['#98abc5', '#8a89a6', '#7b6888', '#6b486b', '#a05d56']);

          // Compute the position of each group on the pie:
          var pie = d3.pie().value(function (d) {
            return d[1];
          });

          var data_ready = pie(Object.entries(data));

          // Build the pie chart: Basically, each part of the pie is a path that we build using the arc function.
          svg
            .selectAll('whatever')
            .data(data_ready)
            .enter()
            .append('path')
            .attr(
              'd',
              d3
                .arc()
                .innerRadius(100) // This is the size of the donut hole
                .outerRadius(radius)
            )
            .attr('fill', function (d) {
              return color(d.data[0]);
            })
            .attr('stroke', 'black')
            .style('stroke-width', '2px')
            .style('opacity', 0.7);
    }
  },
  computed: {
    ...mapState([
      'searchword',
    ]),
    }
};
</script>
