<template>
  <base-card dark>
    <v-img
      :src="require('@/assets/articles/blurcamera.jpg')"
      class="grey lighten-2"
      height="400"
      width="100%"
    >
      <v-row
        class="fill-height pa-3"
        align="center"
      >
        <v-col>
          <h1 class="font-weight-light Jua center">
            언성(UnSung)
          </h1>
          <h4 class="Jua center">언론의 성향을 찾아드립니다!</h4>
          <br>
          <v-form class="center Jua">
          <input
            v-model="keyword"
            class="input"
            placeholder=" 키워드를 입력하세요"
            style="display: inline-block;"

          />
          <v-btn
            color="white"
            icon
            x-large
            plain
            @click="search"
            @keyup.enter="search"
            style="margin-left:20px;"
          >Search
          </v-btn>
          </v-form>
          <v-overflow-btn
            class="my-2 center Jua"
            :items="text"
            label="실시간 검색어"
            target="#dropdown-example-2"
            style="max-width:290px;"
      ></v-overflow-btn>
        </v-col>
      </v-row>
    </v-img>
  </base-card>
</template>

<script>
import http from "@/util/http-common";
import "@/css/font.css";

import {mapState} from 'vuex';

  export default {
    name: 'HomeBanner',

    data(){
      return {
      searched: false,
      text:[],
      keyword:""
      }
    },
    mounted(){
      this.realtimeKeyword();
    },
    methods:{
      realtimeKeyword(){
        http.get(`/api/keyword/search`).then(({data})=>{
          for(var i=0; i<data.length; i++){
          this.text.push((i+1)+ "위" + " " + data[i].keyword);
          if(i>6){
            break;
          }
          }
        })
      },
      search(){
        if(this.keyword == ""){
          alert("검색어를 입력해주세요");
          // this.$router.go;
        }
        else{
          this.$store.dispatch('SearchKeyword', this.keyword)
        // this.searched = true;
        http.post(`/api/keyword/search`, JSON.stringify(this.keyword)).then((data)=>{
          console.log(data);
          console.log(JSON.stringify(this.keyword));
        }, error => {
          console.log(error);
        });
        }
      }

    },
    computed: {
      ...mapState([
        'searchword',
      ]),
    }
  }
</script>
