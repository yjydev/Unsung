<template>
<v-col
    cols="12"
    :md="size === 2 ? 6 : size === 3 ? 4 : undefined"
  >
  <base-card
      :height="value.prominent ? 600 : 500"
      color="white"
    >
    <v-chip
      label
      color="grey darken-3"
      background-color="white"
      text-color="white"
      small
      @click.stop=""
      >최근 Top 키워드
    </v-chip>
  <div id="word-cloud">
  </div>
  </base-card>
</v-col>
</template>

<script>
import http from "@/util/http-common";
import "@/css/font.css";

  export default {
    name: 'WordCloud',
    data() {
      return {
        words:{
          keyword: [],
          count : []
        }

      }
    },
    mounted(){
      this.getKeyWordList();
    },
    methods:{
      getKeyWordList(){
        http.get(`/api/keywordratio/search/wordcount`).then(({data})=>{
        this.words = data;
        // console.log(this.words);
        this.genLayout();
      })
      },
      genLayout(){
        const cloud = require("d3-cloud");
        cloud()
          .words(this.words)
          .padding(1)
          .font("Jua")
          .fontSize(function (data) {
            return data.count * 2;
          })
          .on("end", this.end)
          .spiral("archimedean")
          .start()
          .stop();
      },
      end(words){
        const d3 = require("d3");
        const width = 870;
        const height = 350;

        d3.select("#word-cloud")
          .append("svg")
          .attr("width", width)
          .attr("height", height)
          .style("background", "white")
          .append("g")
          .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
          .selectAll("text")
          .data(words)
          .enter()
          .append("text")
          .style("font-family", "Jua")
          .style("font-size", (data) => {
            return data.count * 2 + "px";
          })
          .style("fill", function(data){
            if(parseInt(data.count) >= parseInt(20)) return "#dd3333";
            else if(parseInt(data.count) > parseInt(15)) return "#8224e3";
            else if(parseInt(data.count) > parseInt(10)) return "#81d742";
            else "#025275";
          })
          .attr("text-anchor", "middle")
          .attr("transform", (data) => {
            return "translate(" + [data.x, data.y] + ")rotate(" + data.rotate + ")";
          })
          .text((data)=>data.keyword);
      },
    },
    props: {
      size: {
        type: Number,
        required: true,
      },
      value: {
        type: Object,
        default: () => ({}),
      },
    },
  }
</script>

<style>

.v-image__image {
  transition: .3s linear;
}

</style>
