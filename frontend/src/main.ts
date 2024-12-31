import 'vue3-toastify/dist/index.css'

import {createApp} from 'vue'
import {createPinia} from 'pinia'
import Vue3Toastify from 'vue3-toastify'
import {toastOptions} from '@/plugin/toastify'
import App from './App.vue'
import router from './router'
import {vuetify} from "@/plugin/vuetify.ts";
import {VueQueryPlugin} from "@tanstack/vue-query";

const app = createApp(App)

app
  .use(router)
  .use(createPinia())
  .use(vuetify)
  .use(Vue3Toastify, toastOptions)
  .use(VueQueryPlugin)
  .mount('#app')
