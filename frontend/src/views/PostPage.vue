<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, watch} from "vue";
import Image from "@/components/Image.vue";
import * as AxiosQuery from "@/api/axios-client.ts"

const route = useRoute()
const router = useRouter()

const {
  data: postDetails,
  isPending,
  error,
  isError
} = AxiosQuery.Query.useGetPostQuery('admin', route.params.id as string)

watch(() => isError.value, (newVal) => {
  if (newVal) {
    setTimeout(() => router.push('/'), 5000)
  }
})

onMounted(() => {
  if (!route.params.id) router.push('/')
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
      <v-col v-if="postDetails.images && postDetails.images.length > 0"
             v-for="image in postDetails.images"
             :cols="12 / postDetails.images.length">
        <Image :image-id="image.id!"/>
      </v-col>
      <v-col v-else>
        沒有圖片
      </v-col>
    </v-row>
    <v-row v-else-if="!postDetails && !isPending">
      <v-col cols="12">
        <v-alert type="error">
          Post not found {{ error }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col cols="6">
        <v-skeleton-loader type="card"/>
      </v-col>
      <v-col cols="6">
        <v-skeleton-loader type="card"/>
      </v-col>
      <v-col cols="6">
        <v-skeleton-loader type="card"/>
      </v-col>
      <v-col cols="6">
        <v-skeleton-loader type="card"/>
      </v-col>
    </v-row>
  </v-container>
</template>
