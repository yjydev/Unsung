<template>
<v-col
    cols="12"
  >
  <base-card
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
   <div>
      <wordcloud
      :data="defaultWords"
      nameKey="keyword"
      valueKey="count"
      color="Category10"
      font="Jua"
      :showTooltip="true"
      :wordClick="wordClickHandler">
      </wordcloud>
  </div>
  </base-card>
</v-col>

</template>

<script>
import wordcloud from 'vue-wordcloud'
import http from "@/util/http-common"
import "@/css/font.css";
export default {
  name: 'WordCloud',
  components: {
    wordcloud
  },
  mounted(){
    this.getKeyWord();
  },
  methods: {
    getKeyWord(){
      http.get(`/api/keywordratio/search/wordcount`).then(({data})=>{
        this.defaultWords = data;
      })
    },
    wordClickHandler(keyword, count, vm) {
      console.log('wordClickHandler', keyword, count, vm);
    }
  },
  data() {
    return {
      defaultWords: [{
          "keyword": [],
          "count": []
        },
      ]
    }
  }
}
</script>
