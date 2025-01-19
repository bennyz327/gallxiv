<script setup lang="ts">
import {RouterView} from 'vue-router'
import {useUserStore} from "@/stores/user.ts";
import {onMounted} from "vue";
import {toast} from "vue3-toastify";
import {VueQueryDevtools} from "@tanstack/vue-query-devtools";

const userStore = useUserStore()

onMounted(async () => {
  await userStore.init()
  toast(`已使用 ${userStore.user?.username} 登入`)
})
</script>

<template>
  <v-app>
    <RouterView/>
  </v-app>

  <VueQueryDevtools/>
</template>

<style scoped>

</style>
