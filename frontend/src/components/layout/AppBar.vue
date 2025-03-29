<script setup lang="ts">
import {useRouter} from "vue-router";
import {useLogout} from "@/api/generated";
import {useUserStore} from "@/stores/user";
import {toast} from 'vue3-toastify';
import {defineProps, ref} from 'vue';

const props = defineProps({
  title: {
    type: String,
    default: 'Gallexiv',
  },
  showBackButton: {
    type: Boolean,
    default: false,
  },
});

const {mutateAsync: logout} = useLogout();

const router = useRouter();
const userStore = useUserStore();
const logoutLoading = ref(false);

const goToHome = () => {
  router.push('/');
};

const goToLogin = () => {
  router.push('/login');
};

const goToCreatePost = () => {
  router.push('/create-post');
};

const handleLogout = async () => {
  try {
    logoutLoading.value = true;
    await logout();
    await userStore.logoutCallback();
    toast('已成功登出');
    // 使用 Vue Router 導航到首頁，不再強制重新整理
    await router.push('/');
  } catch (error) {
    console.error('登出錯誤:', error);
  } finally {
    logoutLoading.value = false;
  }
};
</script>

<template>
  <v-app-bar>
    <template v-slot:title>
      <span @click="goToHome" class="cursor-pointer">{{ title }}</span>
      <v-btn icon @click="goToHome" v-if="showBackButton">
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
    </template>
    <template v-slot:append>
      <div v-if="userStore.isLoggedIn" class="d-flex align-center">
        <span class="me-4 text-white">歡迎, {{ userStore.user?.username }}</span>
        <v-btn
          color="success"
          prepend-icon="mdi-plus"
          class="me-4"
          @click="goToCreatePost"
        >
          發佈新貼文
        </v-btn>
        <v-badge v-if="userStore.isAdmin" color="error" content="管理員" class="me-4"></v-badge>
        <v-btn
          color="error"
          variant="tonal"
          :loading="logoutLoading"
          @click="handleLogout"
        >登出
        </v-btn>
      </div>
      <v-btn
        v-else
        color="login-primary"
        :class="{ 'text-white': true }"
        @click="goToLogin"
      >登入
      </v-btn>
    </template>
    <slot></slot>
  </v-app-bar>
  <div style="height: 64px"></div>
</template>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}
</style>
