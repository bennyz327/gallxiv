<script setup lang="ts">
import {computed} from "vue";
import {useGetImage} from "@/api/generated";

const props = defineProps<{ imageId: string }>()

const {data} = useGetImage(props.imageId, {
  query: {staleTime: 1000 * 10}
})

const imageUrl = computed(() => data.value ? URL.createObjectURL(data.value) : undefined)
</script>

<template>
  <v-img v-if="imageId" :src="imageUrl" alt="讀取失敗"/>
  <v-skeleton-loader v-else/>
</template>
