import Vue from 'vue'
import vuetify from './plugins/vuetify'
import './plugins/base'
import App from './App.vue'
import router from './router'
import store from './store'
import VueWordCloud from 'vuewordcloud'

Vue.config.productionTip = false

new Vue({
  vuetify,
  router,
  store,
  VueWordCloud,
  render: h => h(App),
}).$mount('#app')
