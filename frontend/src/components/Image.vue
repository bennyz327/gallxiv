<script setup lang="ts">
import {computed} from "vue";
import * as AxiosQuery from "@/api/axios-client.ts"

const props = defineProps<{ imageId: string }>()
const {data} = AxiosQuery.Query.useGetImageQuery(props.imageId, { staleTime: 1000 * 10 })
const imageUrl = computed(() => data.value?.data ? URL.createObjectURL(data.value?.data) : undefined)
</script>

<template>
  <v-img v-if="imageId" :src="imageUrl" alt="讀取失敗"/>
  <v-skeleton-loader v-else />
</template>
