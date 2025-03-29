<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import { onMounted, watch } from "vue";
import Image from "@/components/Image.vue";
import { useGetPost } from "@/api/generated";
import { useUserStore } from "@/stores/user";
import AppBar from "@/components/layout/AppBar.vue";

const route = useRoute();
const router = useRouter();

const { data: postDetails, isPending, isError, error } = useGetPost(
  route.params.account as string,
  route.params.id as string,
);

watch(() => isError.value, (newVal) => {
  if (newVal) {
    setTimeout(() => router.push('/'), 5000);
  }
});

onMounted(() => {
  if (!route.params.id) router.push('/');
});
</script>

<template>
  <v-container>
    <AppBar title="Gallexiv" :showBackButton="true" />

    <v-row v-if="postDetails">
      <v-col cols="12">
        <v-card>
          <v-card-title>
            {{ postDetails.title }}
          </v-card-title>
          <v-card-text>
            {{ postDetails.description }}
          </v-card-text>
          <v-card-text v-if="postDetails.user">
            <strong>作者:</strong> {{ postDetails.user.username }} ({{ postDetails.user.account }})
          </v-card-text>
        </v-card>
      </v-col>
      <v-col v-if="postDetails.images && postDetails.images.length > 0"
             v-for="image in postDetails.images"
             :key="image.id"
             :cols="12 / postDetails.images.length">
        <Image :post-id="postDetails.id!" :image-id="image.id!"/>
      </v-col>
      <v-col v-else>
        沒有圖片
      </v-col>
    </v-row>
    <v-row v-else-if="!postDetails && !isPending">
      <v-col cols="12">
        <v-alert type="error">
          找不到此貼文 {{ error }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row v-else>
      <v-col cols="12" class="d-flex justify-center">
        <v-progress-circular indeterminate></v-progress-circular>
      </v-col>
    </v-row>
  </v-container>
</template>
