<script setup lang="ts">
import {useRouter} from "vue-router"
import {useGetPosts} from "@/api/generated";

const router = useRouter()

const goToPostPage = (postId: string) => {
  router.push(`/post/${postId}`)
}

const {data: posts} = useGetPosts()
</script>

<template>
  <v-container>
    <v-app-bar title="Gallexiv"></v-app-bar>
    <div style="height: 64px"></div>
    <v-row>
      <v-col cols="12">
        <h1>貼文列表</h1>
      </v-col>
    </v-row>
    <v-row v-if="posts && posts.length > 0">
      <v-col v-for="post in posts" cols="12" md="12" :key="post.id">
        <v-card @click="goToPostPage(post.id!)">
          <v-card-title>{{ post.title }}</v-card-title>
          <v-card-text>作者: {{ post.user!.account }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
