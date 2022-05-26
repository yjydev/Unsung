<template>
  <v-container>
    <v-row>
      <v-col cols="12">
        <slot />
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <word-cloud
          :size="layout[0]"
        />
      </v-col>
      <v-col cols="6">
        <news
          :size="layout[5]"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <chart />
      </v-col>
      <v-col cols="6">
        <normal-chart />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <trans-chart />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  // Utilities

  export default {
    name: 'Feed',

    components: {
      WordCloud: () => import('@/components/NewWordCloud'),
      News: () => import('@/components/News'),
      Chart: () => import('@/components/Chart'),
      NormalChart: () => import('@/components/NormalChart'),
      TransChart: () => import('@/components/TransChart'),
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
