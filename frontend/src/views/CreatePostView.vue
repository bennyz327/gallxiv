<script setup lang="ts">
import {ref, onMounted, onBeforeUnmount} from 'vue';
import {useRouter, onBeforeRouteLeave} from 'vue-router';
import {useUserStore} from '@/stores/user';
import {toast} from 'vue3-toastify';
import type {UserPostMetadataRequest} from '@/api/generated/types/UserPostMetadataRequest';
import {useUserPost} from '@/api/generated-override';
import AppBar from "@/components/layout/AppBar.vue";

const router = useRouter();
const userStore = useUserStore();

// 表單數據
const title = ref('');
const description = ref('');
const tags = ref('');
const images = ref<File[]>([]);
const previewUrls = ref<string[]>([]);
const isPublic = ref(true); // 默認為公開

// 處理狀態
const loading = ref(false);
const error = ref('');
const formChanged = ref(false);

// 使用自定義的貼文上傳API
const {mutateAsync: createPost} = useUserPost();

// 檔案輸入參考
const fileInput = ref<HTMLInputElement | null>(null);

// 監控表單是否有變更
const checkFormChanged = () => {
  return title.value !== '' || 
         description.value !== '' || 
         tags.value !== '' || 
         images.value.length > 0;
};

// 設置 beforeunload 事件處理
const handleBeforeUnload = (e: BeforeUnloadEvent) => {
  if (checkFormChanged()) {
    // 標準格式的離開提醒訊息
    const message = '您有未儲存的變更，確定要離開此頁面嗎？';
    e.preventDefault();
    e.returnValue = message; // 標準寫法
    return message; // 為了相容舊版瀏覽器
  }
};

// 監控路由變化
onBeforeRouteLeave((to, from, next) => {
  if (checkFormChanged() && !loading.value) {
    if (window.confirm('您有未儲存的變更，確定要離開此頁面嗎？')) {
      next();
    } else {
      next(false);
    }
  } else {
    next();
  }
});

// 組件掛載時添加事件監聽
onMounted(() => {
  window.addEventListener('beforeunload', handleBeforeUnload);
});

// 組件卸載時移除事件監聽
onBeforeUnmount(() => {
  window.removeEventListener('beforeunload', handleBeforeUnload);
});

// 觸發檔案選擇對話框
const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

// 選擇圖片
const handleFileChange = (event: Event) => {
  const inputElement = event.target as HTMLInputElement;
  if (!inputElement.files || inputElement.files.length === 0) return;

  // 清除之前的預覽
  previewUrls.value.forEach(url => URL.revokeObjectURL(url));
  previewUrls.value = [];

  // 保存文件並創建預覽
  images.value = Array.from(inputElement.files);
  images.value.forEach((file) => {
    previewUrls.value.push(URL.createObjectURL(file));
  });
  formChanged.value = true;
};

// 移除選擇的圖片
const removeImage = (index: number) => {
  URL.revokeObjectURL(previewUrls.value[index]);
  previewUrls.value.splice(index, 1);
  images.value.splice(index, 1);

  // 如果已移除所有圖片，重置 file input 元素
  if (images.value.length === 0 && fileInput.value) {
    fileInput.value.value = '';
  }
};

// 提交表單
const handleSubmit = async () => {
  if (!title.value) {
    error.value = '請填寫貼文標題';
    return;
  }

  if (images.value.length === 0) {
    error.value = '請至少選擇一張圖片';
    return;
  }

  try {
    loading.value = true;
    error.value = '';

    const metadata: UserPostMetadataRequest = {
      title: title.value,
      description: description.value,
      tags: tags.value,
      isPublic: isPublic.value,
    };

    // 提交貼文
    const postId = await createPost({
      data: {
        metadata,
        images: images.value,
      },
    });

    // 表單提交成功後，將表單狀態重置
    formChanged.value = false;
    toast.success('貼文發布成功！');
    await router.push(`/post/${userStore.user?.account}/${postId}`);
  } catch (err: any) {
    error.value = err.message || '上傳失敗，請稍後再試';
    toast.error('貼文發布失敗');
    console.error('上傳錯誤:', err);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <v-container>
    <AppBar title="Gallexiv - 發布新貼文" :showBackButton="true" />

    <v-row>
      <v-col cols="12">
        <v-card class="pa-4">
          <v-form @submit.prevent="handleSubmit">
            <v-alert v-if="error" type="error" variant="tonal" class="mb-4">
              {{ error }}
            </v-alert>

            <v-text-field
              v-model="title"
              label="貼文標題"
              required
              :disabled="loading"
              class="mb-3"
            ></v-text-field>

            <v-textarea
              v-model="description"
              label="貼文描述"
              required
              :disabled="loading"
              class="mb-3"
            ></v-textarea>

            <v-text-field
              v-model="tags"
              label="標籤 (用逗號分隔)"
              placeholder="例如: 風景,攝影,自然"
              required
              :disabled="loading"
              class="mb-3"
              hint="請使用逗號分隔多個標籤"
            ></v-text-field>

            <!-- 隱藏的檔案輸入元素 -->
            <input
              ref="fileInput"
              type="file"
              multiple
              accept="image/*"
              @change="handleFileChange"
              style="display: none"
            />

            <!-- 自訂檔案選擇按鈕 -->
            <v-btn
              color="primary"
              prepend-icon="mdi-image-plus"
              @click="triggerFileInput"
              :disabled="loading"
              class="mb-4"
            >
              選擇圖片
            </v-btn>

            <!-- 提示用戶已選擇的檔案數量 -->
            <span v-if="images.length > 0" class="ml-4 text-body-2">
              已選擇 {{ images.length }} 張圖片
            </span>

            <!-- 圖片預覽 -->
            <v-row v-if="previewUrls.length > 0" class="mt-3">
              <v-col v-for="(url, index) in previewUrls" :key="index" cols="6" md="3">
                <v-card>
                  <v-img :src="url" height="200" cover></v-img>
                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="error" icon @click="removeImage(index)">
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-col>
            </v-row>

            <div class="d-flex justify-end mt-4">
              <v-btn
                type="submit"
                color="primary"
                size="large"
                :loading="loading"
                :disabled="loading"
              >
                發佈貼文
              </v-btn>
            </div>
          </v-form>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
