<script setup lang="ts">
import {useRouter} from "vue-router";
import {useGetPosts} from "@/api/generated";
import AppBar from "@/components/layout/AppBar.vue";

const router = useRouter();

const goToPostPage = (account: string, postId: string) => {
  router.push(`/post/${account}/${postId}`);
};

const { data: posts, isPending, isError } = useGetPosts();
</script>

<template>
  <v-container>
    <AppBar title="Gallexiv" />

    <v-row>
      <v-col cols="12" class="d-flex justify-space-between align-center">
        <h1>貼文列表</h1>
      </v-col>
    </v-row>
    <v-row v-if="!isPending && posts && posts.length > 0">
      <v-col v-for="post in posts" cols="12" md="12" :key="post.id">
        <v-card @click="goToPostPage(post.user!.account!, post.id!)">
          <v-card-title>{{ post.title }}</v-card-title>
          <v-card-text>作者: {{ post.user?.account }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <v-row v-else-if="isError">
      <v-col cols="12" class="text-center">
        <p>無法載入貼文列表，請稍後再試</p>
      </v-col>
    </v-row>
    <v-row v-else-if="isPending">
      <v-col cols="12" class="text-center">
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col cols="12" class="text-center">
        <p>暫無貼文</p>
      </v-col>
    </v-row>
  </v-container>
</template>
