<template>
<v-col
    cols="12"
    :md="size === 2 ? 6 : size === 3 ? 4 : undefined"
  >
  <base-card
      :height="value.prominent ? 600 : 500"
    >
    <v-chip
      label
      color="grey darken-3"
      text-color="white"
      small
      >최신 뉴스
    </v-chip>
    <v-row
      v-for="(newspaper, i) in news"
      :key="i"
      align="center"
      class="mb-2"
    >
      <v-col
        v-if="i<4"
        class="d-flex"
        cols="12"
        offset-md="0"
      >
        <v-img
          v-if="newspaper.image" :src="newspaper.image"
          class="mr-3"
          height="80"
          max-width="80"
        />
        <v-img
          v-if="!newspaper.image"
          :src="require('@/assets/no_image.jpg')"
          class="mr-3"
          height="80"
          max-width="80"
        />
        <div>
          <div class="subheading Jua">
            언론사 : {{newspaper.press}}
          </div>
          <div class="Jua">
            기사제목 : {{newspaper.title}}
          </div>
          <div class="Jua">
            <a :href="newspaper.url">기사링크</a>
          </div>
        </div>
      </v-col>
    </v-row>

  </base-card>
</v-col>
</template>

<script>
import http from "@/util/http-common";
import "@/css/font.css";

  export default {
    name: 'News',
    data() {
      return {
        news:{
          image:[],
          title:[],
          press:[],
          url:[],
          num:[]
        }
      }
    },
    created(){
      this.latestnews();
    },
    methods:{
      latestnews(){
        http.get(`api/newnews/news`).then(({data})=>{
          this.news = data;
          console.log(data);
        })
      }
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
