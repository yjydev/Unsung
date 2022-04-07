import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchword : null,
  },
  mutations: {
    SEARCH_KEYWORD:function(state, searchword){
      state.searchword = searchword
    },
  },
  actions: {
    SearchKeyword : function({commit}, searchword){
      commit('SEARCH_KEYWORD', searchword)
    }

  },


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
