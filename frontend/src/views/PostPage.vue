<script setup lang="ts">
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
import axios from "axios";

const route = useRoute()

const postFetching = ref(true)

const postDetails = ref()

onMounted(async () => {
  postFetching.value = true
  const postId = route.params.id ? String(route.params.id) : undefined
  console.log(postId)
  postDetails.value = (await axios.get<any>(`/user/admin/post/${postId}`)).data
  console.log(postDetails)
  postFetching.value = false
})
</script>

<template>
  <v-container>
    <v-row v-if="postDetails">
      <v-col cols="12">
        <v-card>
          <v-card-title>
            {{ postDetails.title }}
          </v-card-title>
          <v-card-text>
            {{ postDetails.description }}
          </v-card-text>
        </v-card>
      </v-col>
      <v-col v-for="image in postDetails.images" :cols="12 / postDetails.images.length">
        <v-img :src="`/image/${image.id}`"/>
      </v-col>
    </v-row>

    <v-row v-else-if="!postDetails && !postFetching">
      <v-col cols="12">
        <v-alert type="error">
          Post not found
        </v-alert>
      </v-col>
    </v-row>

    <v-row v-else>
      <v-col cols="12">
        讀取中...
      </v-col>
    </v-row>
  </v-container>
</template>
