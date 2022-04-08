import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import axios from 'axios'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchword : null,
    normal_data : {'keyword':'','jtbc': 0, 'sbs': 0, 'middle': 0, 'kbs': 0},
    trans_data : {'keyword':'',
    '2021-01': '', '2021-02': '', '2021-03': '', '2021-04': '', '2021-05': '', '2021-06': '',
    '2021-07': '', '2021-08': '', '2021-09': '', '2021-10':'','2021-11': '', '2021-12': '',
    '2022-01': '', '2022-02': '', '2022-03': ''
    },
    trans_press: '',
    doughnut_data_JTBC: {'positive' : 0, 'negative' : 0, 'neutral' : 0, 'unclassified' : 0},
    doughnut_data_SBS: {'positive' : 0, 'negative' : 0, 'neutral' : 0, 'unclassified' : 0},
    doughnut_data_MIDDLE: {'positive' : 0, 'negative' : 0, 'neutral' : 0, 'unclassified' : 0},
    doughnut_data_KBS: {'positive' : 0, 'negative' : 0, 'neutral' : 0, 'unclassified' : 0}
  },
  mutations: {
    SEARCH_KEYWORD:function(state, searchword){
      state.searchword = searchword
    },
    UPDATE_PRESS : function(state, press){
      state.trans_press = press
    },
    // GET_CHAT_DATA: function(state, data){
    //   data.forEach(d => {
    //     state.normal_data.push(d.total)
    //   })
    //   console.log(state.normal_data)
    // },
    GET_CHAT_DATA: function(state, data){
      state.normal_data = {'jtbc': 0,'sbs':0,'middle':0, 'kbs':0}
      state.normal_data['jtbc'] = data[0]['total']
      state.normal_data['sbs'] = data[2]['total']
      state.normal_data['middle'] = data[3]['total']
      state.normal_data['kbs'] = data[1]['total']
      state.normal_data['keyword'] = data[0]['keyword']
      // console.log(state.normal_data)
    },
    GET_TRANS_CHAT: function(state, data){
      data.forEach(d=>{
        state.trans_data[d['period']] = [d['positive'], d['negative'], d['total']]
        state.trans_data['keyword'] = d['keyword']
      })
    },
    GET_DOUGHNUT_DATA: function(state, data){

          state.doughnut_data_JTBC['positive'] = data[0]['positive']
          state.doughnut_data_JTBC['negative'] = data[0]['negative']
          state.doughnut_data_JTBC['neutral'] = data[0]['neutral']
          state.doughnut_data_JTBC['unclassified'] = data[0]['unclassified']

          state.doughnut_data_SBS['positive'] = data[1]['positive']
          state.doughnut_data_SBS['negative'] = data[1]['negative']
          state.doughnut_data_SBS['neutral'] = data[1]['neutral']
          state.doughnut_data_SBS['unclassified'] = data[1]['unclassified']

          state.doughnut_data_MIDDLE['positive'] = data[2]['positive']
          state.doughnut_data_MIDDLE['negative'] = data[2]['negative']
          state.doughnut_data_MIDDLE['neutral'] = data[2]['neutral']
          state.doughnut_data_MIDDLE['unclassified'] = data[2]['unclassified']

          state.doughnut_data_KBS['positive'] = data[3]['positive']
          state.doughnut_data_KBS['negative'] = data[3]['negative']
          state.doughnut_data_KBS['neutral'] = data[3]['neutral']
          state.doughnut_data_KBS['unclassified'] = data[3]['unclassified']

      // state.doughnut_data = {'positive' : 0, 'negative' : 0, 'neutral' : 0, 'unclassified' : 0}
      // state.doughnut_data['positive'] = data[0]['positive']
      // state.doughnut_data['negative'] = data[0]['negative']
      // state.doughnut_data['neutral'] = data[0]['neutral']
      // state.doughnut_data['unclassified'] = data[0]['unclassified']
    }
  },
  actions: {
    SearchKeyword : function({commit}, searchword){
      commit('SEARCH_KEYWORD', searchword)
    },
    GetChat : function({commit}, searchword){
      axios({
        method:'get',
        url:`http://j6b207.p.ssafy.io:9090/api/keywordratio/search/graph/${searchword}`
      }).then(res=>{
        commit('GET_CHAT_DATA', res.data)
      })
    },
    GetTransChart: function({commit}, [word, press]){
      axios({
        method:'get',
        url:`http://j6b207.p.ssafy.io:9090/api/keywordratio/search/graph/${word}/${press}`
      }).then(res=>{
        commit('GET_TRANS_CHAT', res.data)
        // console.log(res.data)
      })
    },
    UpdatePress: function({commit}, press){
      commit('UPDATE_PRESS', press)
    },
    GetDoughnutData: function({commit}, searchword){
      axios({
        method: 'get',
        url:`http://j6b207.p.ssafy.io:9090/api/keywordratio/search/${searchword}`
      }).then(res=>{
        commit('GET_DOUGHNUT_DATA', res.data)
      })
    }
  },
  plugins: [
    createPersistedState()
  ]

  // getters: {
  //   categories: state => {
  //     const categories = []

  //     for (const article of state.articles) {
  //       if (
  //         !article.category ||
  //         categories.find(category => category.text === article.category)
  //       ) continue

  //       const text = article.category

  //       categories.push({
  //         text,
  //         href: '#!',
  //       })
  //     }

  //     return categories.sort().slice(0, 4)
  //   },
  //   links: (state, getters) => {
  //     return state.items.concat(getters.categories)
  //   },
  // },
})
