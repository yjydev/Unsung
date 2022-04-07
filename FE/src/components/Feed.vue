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
      <chart
      />
      <normal-chart
      />
    </v-row>
  </v-container>
</template>

<script>
  // Utilities

  export default {
    name: 'Feed',

    components: {
      WordCloud: () => import('@/components/WordCloud'),
      News: () => import('@/components/News'),
      Chart: () => import('@/components/Chart'),
      NormalChart: () => import('@/components/NormalChart')
    },

    data: () => ({
      layout: [2, 2, 1, 2, 2, 3, 3, 3, 3, 3, 3],
      page: 1,
    }),


    watch: {
      page () {
        window.scrollTo(0, 1)
      },
    },
  }
</script>
