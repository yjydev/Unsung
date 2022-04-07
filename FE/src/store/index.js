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
    trans_press: ''
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
