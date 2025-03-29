<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useLogin } from '@/api/generated';
import { useUserStore } from '@/stores/user';
import { toast } from 'vue3-toastify';

const router = useRouter();
const userStore = useUserStore();

const account = ref('');
const password = ref('');
const loading = ref(false);
const error = ref('');
const showPassword = ref(false);

const { mutateAsync: login } = useLogin();

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

const handleLogin = async () => {
  if (!account.value || !password.value) {
    error.value = '帳號和密碼不能為空';
    return;
  }

  try {
    loading.value = true;
    error.value = '';

    await login({
      data: {
        account: account.value,
        password: password.value
      }
    });

    // 登入成功後初始化用戶資料
    await userStore.init();
    toast('登入成功');
    await router.push('/');
  } catch (err: any) {
    error.value = err.message || '登入失敗，請檢查帳號密碼';
  } finally {
    loading.value = false;
  }
};

// 返回首頁
const goToHome = () => {
  router.push('/');
};
</script>

<template>
  <v-container fluid class="fill-height">
    <v-row align="center" justify="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12">
          <v-card-title class="text-center py-4">
            <h1 class="text-h5">登入 Gallexiv</h1>
          </v-card-title>
          <v-card-text>
            <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
              {{ error }}
            </v-alert>
            <v-form @submit.prevent="handleLogin">
              <div class="login-fields">
                <v-text-field
                  v-model="account"
                  label="帳號"
                  prepend-icon="mdi-account"
                  variant="outlined"
                  required
                  class="mb-4"
                  hide-details
                ></v-text-field>
                <v-text-field
                  v-model="password"
                  label="密碼"
                  prepend-icon="mdi-lock"
                  :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="togglePasswordVisibility"
                  variant="outlined"
                  :type="showPassword ? 'text' : 'password'"
                  required
                  class="mb-6"
                  hide-details
                ></v-text-field>
              </div>
              <div class="d-flex justify-center">
                <v-btn
                  color="primary"
                  size="large"
                  type="submit"
                  :loading="loading"
                  block
                >
                  登入
                </v-btn>
              </div>
              <div class="d-flex justify-center mt-4">
                <v-btn
                  color="secondary"
                  variant="tonal"
                  size="small"
                  @click="goToHome"
                  block
                >
                  返回首頁
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.login-fields {
  width: 100%;
}

.login-fields :deep(.v-field__prepend-inner) {
  padding-right: 12px;
}

.login-fields :deep(.v-field__append-inner) {
  padding-left: 12px;
}
</style>
