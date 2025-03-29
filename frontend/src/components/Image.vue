<script setup lang="ts">
import {computed} from "vue";
import {useGetImage} from "@/api/generated";

const props = defineProps<{ postId: string, imageId: string }>()

const {data} = useGetImage(props.postId, props.imageId, {
  query: {staleTime: 1000 * 10},
  client: {responseType: 'blob'},
})

const imageUrl = computed(() => data.value ? URL.createObjectURL(data.value) : '')
</script>

<template>
  <v-img v-if="props.imageId" :src="imageUrl" alt="讀取失敗"/>
  <v-skeleton-loader v-else/>
</template>
