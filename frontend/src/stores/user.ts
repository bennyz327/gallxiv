import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from "axios";

export type User = {
  id: string
  account: string
  username: string
  role: 'ROOT' | 'USER'
}

export const useUserStore = defineStore('user', () => {

  const user = ref<User>()

  // GET request to fetch user data
  const init = async () => {
    // request login to get Set-Cookie
    await axios.post('/api/auth/login', {
      account: 'admin',
      password: 'admin'
    })
    const response = await axios<User>('/api/me', {
      withCredentials: true
    })
    user.value = response.data
  }

  return {
    user,
    init
  }
})
