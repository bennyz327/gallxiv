<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" md="8" lg="6">
        <div class="d-flex align-center mb-4">
          <v-btn icon="mdi-arrow-left" variant="text" @click="goBack" class="mr-2"></v-btn>
          <h1>使用者設定</h1>
        </div>

        <v-card class="mt-6 elevation-2">
          <v-card-title class="text-center py-4">
            <h2 class="text-h6">修改密碼</h2>
          </v-card-title>
          <v-card-text>
            <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
              {{ error }}
            </v-alert>

            <v-form @submit.prevent="handleChangePassword">
              <div class="change-password-fields">
                <v-text-field
                  v-model="currentPassword"
                  label="當前密碼"
                  prepend-icon="mdi-lock"
                  :append-inner-icon="showCurrentPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="toggleCurrentPasswordVisibility"
                  variant="outlined"
                  :type="showCurrentPassword ? 'text' : 'password'"
                  required
                  class="mb-3"
                  hide-details
                ></v-text-field>

                <v-text-field
                  v-model="newPassword"
                  label="新密碼"
                  prepend-icon="mdi-lock-plus"
                  :append-inner-icon="showNewPassword ? 'mdi-eye-off' : 'mdi-eye'"
                  @click:append-inner="toggleNewPasswordVisibility"
                  variant="outlined"
                  :type="showNewPassword ? 'text' : 'password'"
                  required
                  class="mb-3"
                  hide-details
                ></v-text-field>

                <v-text-field
                  v-model="confirmPassword"
                  label="確認新密碼"
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
                  修改密碼
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>

      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useChangePassword } from '@/api/generated-override';
import { toast } from 'vue3-toastify';
import {useRouter} from 'vue-router'

// 表單數據
const currentPassword = ref('');
const newPassword = ref('');
const confirmPassword = ref('');

// 處理狀態
const loading = ref(false);
const error = ref('');
const showCurrentPassword = ref(false);
const showNewPassword = ref(false);
const showConfirmPassword = ref(false);

const { mutateAsync: changePassword } = useChangePassword();
const router = useRouter();

const goBack = () => {
  router.back();
};

const toggleCurrentPasswordVisibility = () => {
  showCurrentPassword.value = !showCurrentPassword.value;
};

const toggleNewPasswordVisibility = () => {
  showNewPassword.value = !showNewPassword.value;
};

const toggleConfirmPasswordVisibility = () => {
  showConfirmPassword.value = !showConfirmPassword.value;
};

const validateForm = () => {
  // 檢查必填項目
  if (!currentPassword.value || !newPassword.value || !confirmPassword.value) {
    error.value = '所有欄位都必須填寫';
    return false;
  }

  // 檢查新密碼長度
  if (newPassword.value.length < 6) {
    error.value = '新密碼至少需要 6 個字元';
    return false;
  }

  // 檢查新密碼和確認密碼是否一致
  if (newPassword.value !== confirmPassword.value) {
    error.value = '新密碼和確認密碼不符';
    return false;
  }

  error.value = ''; // 清除之前的錯誤訊息
  return true;
};

const handleChangePassword = async () => {
  if (!validateForm()) {
    return;
  }

  try {
    loading.value = true;
    error.value = '';

    await changePassword({
      data: {
        currentPassword: currentPassword.value,
        newPassword: newPassword.value
      }
    });

    toast('密碼修改成功！');

    // 清空表單
    currentPassword.value = '';
    newPassword.value = '';
    confirmPassword.value = '';

  } catch (err: any) {
    console.error('密碼修改錯誤:', err);

    // 處理不同的錯誤狀態
    if (err.response?.status === 401) {
      error.value = '當前密碼不正確';
    } else if (err.response?.status === 400) {
      error.value = '新密碼不能與當前密碼相同';
    } else {
      error.value = err.message || '密碼修改失敗，請稍後再試';
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.change-password-fields {
  margin-bottom: 20px;
}
</style>
