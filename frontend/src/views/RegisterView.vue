<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useRegister } from '@/api/generated';
import { useUserStore } from '@/stores/user';
import { toast } from 'vue3-toastify';

const router = useRouter();
const userStore = useUserStore();

const account = ref('');
const email = ref('');
const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const loading = ref(false);
const error = ref('');
const showPassword = ref(false);
const showConfirmPassword = ref(false);

const { mutateAsync: register } = useRegister();

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value;
};

const toggleConfirmPasswordVisibility = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

const validateForm = () => {
  // 檢查必填項目
  if (!account.value || !email.value || !username.value || !password.value || !confirmPassword.value) {
    error.value = '所有欄位都必須填寫';
    return false;
  }

  // 檢查帳號格式
  if (account.value.length < 4) {
    error.value = '帳號至少需要 4 個字元';
    return false;
  }

  // 檢查電子郵件格式
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email.value)) {
    error.value = '請輸入有效的電子郵件地址';
    return false;
  }

  // 檢查使用者名稱長度
  if (username.value.length < 2) {
    error.value = '使用者名稱至少需要 2 個字元';
    return false;
  }

  // 檢查密碼長度
  if (password.value.length < 6) {
    error.value = '密碼至少需要 6 個字元';
    return false;
  }

  // 檢查密碼和確認密碼是否一致
  if (password.value !== confirmPassword.value) {
    error.value = '密碼和確認密碼不符';
    return false;
  }

  return true;
};

const handleRegister = async () => {
  if (!validateForm()) {
    return;
  }

  try {
    loading.value = true;
    error.value = '';

    await register({
      data: {
        account: account.value,
        email: email.value,
        username: username.value,
        password: password.value
      }
    });

    // 註冊成功後初始化用戶資料
    await userStore.init();
    toast('註冊成功！');
    await router.push('/');
  } catch (err: any) {
    console.error('註冊錯誤:', err);
    
    // 處理不同的錯誤狀態
    if (err.response?.status === 409) {
      error.value = '帳號或電子郵件已被使用';
    } else {
      error.value = err.message || '註冊失敗，請稍後再試';
    }
  } finally {
    loading.value = false;
  }
};

// 轉到登入頁面
const goToLogin = () => {
  router.push('/login');
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
            <h1 class="text-h5">註冊 Gallexiv</h1>
          </v-card-title>
          <v-card-text>
            <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
              {{ error }}
            </v-alert>
            <v-form @submit.prevent="handleRegister">
              <div class="register-fields">
                <v-text-field
                  v-model="account"
                  label="帳號"
                  prepend-icon="mdi-account"
                  variant="outlined"
                  required
                  class="mb-3"
                  hide-details
                ></v-text-field>
                <v-text-field
                  v-model="email"
                  label="電子郵件"
                  prepend-icon="mdi-email"
                  variant="outlined"
                  required
                  class="mb-3"
                  hide-details
                ></v-text-field>
                <v-text-field
                  v-model="username"
                  label="使用者名稱"
                  prepend-icon="mdi-badge-account"
                  variant="outlined"
                  required
                  class="mb-3"
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
                  class="mb-3"
                  hide-details
                ></v-text-field>
                <v-text-field
                  v-model="confirmPassword"
                  label="確認密碼"
                  prepend-icon="mdi-lock-check"
                  :append-inner-icon="showConfirmPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="toggleConfirmPasswordVisibility"
                  variant="outlined"
                  :type="showConfirmPassword ? 'text' : 'password'"
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
                  註冊
                </v-btn>
              </div>
              <div class="d-flex justify-center mt-4">
                <v-btn
                  color="secondary"
                  variant="tonal"
                  size="small"
                  @click="goToLogin"
                  class="mr-2"
                >
                  已有帳號？登入
                </v-btn>
                <v-btn
                  color="secondary"
                  variant="tonal"
                  size="small"
                  @click="goToHome"
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
.register-fields {
  width: 100%;
}

.register-fields :deep(.v-field__prepend-inner) {
  padding-right: 12px;
}

.register-fields :deep(.v-field__append-inner) {
  padding-left: 12px;
}
</style>