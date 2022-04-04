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
        <v-col
          cols="12"
          md="7"
          offset-md="5"
        >
          <h1 class="display-3 font-weight-light">
            언성
          </h1>
          <br>
          <v-text-field
            append-icon="mdi-magnify"
            flat
            hide-details
            solo-inverted
            placeholder="키워드를 입력하세요"
            style="max-width: 300px;"
          />
        </v-col>
      </v-row>
    </v-img>
  </base-card>
</template>

<script>
  export default {
    name: 'HomeBanner',
  }
</script>
