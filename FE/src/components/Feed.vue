<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <slot />
      </v-col>
      <word-cloud
        :size="layout[0]"
      />
      <news
        :size="layout[5]"
      />
    </v-row>
  </v-container>
</template>

<script>
  // Utilities
  import {
    mapState,
  } from 'vuex'

  export default {
    name: 'Feed',

    components: {
      WordCloud: () => import('@/components/WordCloud'),
      News: () => import('@/components/News')
    },

    data: () => ({
      layout: [2, 2, 1, 2, 2, 3, 3, 3, 3, 3, 3],
      page: 1,
    }),

    computed: {
      // ...mapState(['articles']),
      // pages () {
      //   return Math.ceil(this.articles.length / 11)
      // },
      // paginatedArticles () {
      //   const start = (this.page - 1) * 11
      //   const stop = this.page * 11

      //   return this.articles.slice(start, stop)
      // },
    },

    watch: {
      page () {
        window.scrollTo(0, 0)
      },
    },
  }
</script>
