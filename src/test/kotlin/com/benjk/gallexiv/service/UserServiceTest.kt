package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.UserRole
import com.benjk.gallexiv.data.dto.RegisterRequest
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.*
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.server.ResponseStatusException
import java.util.*

class UserServiceTest {
    // 建立模擬物件
    private val userRepository: UserRepository = mock()
    private val passwordEncoder: BCryptPasswordEncoder = mock()
    
    // 測試目標
    private lateinit var userService: UserService
    
    @BeforeEach
    fun setUp() {
        userService = UserService(userRepository, passwordEncoder)
    }
    
    @Test
    fun `註冊新使用者成功`() {
        // 設置測試數據
        val registerRequest = RegisterRequest(
            account = "testUser",
            email = "test@example.com",
            username = "Test User",
            password = "password123"
        )
        
        val encodedPassword = "encodedPassword"
        val savedUser = User(
            id = UUID.fromString("11111111-1111-1111-1111-111111111111"),
            account = registerRequest.account,
            email = registerRequest.email,
            username = registerRequest.username,
            password = encodedPassword,
            role = UserRole.USER
        )
        
        // 設置模擬行為
        whenever(userRepository.findByAccount(registerRequest.account))
            .thenReturn(Optional.empty())
        whenever(userRepository.findByEmail(registerRequest.email))
            .thenReturn(Optional.empty())
        whenever(passwordEncoder.encode(registerRequest.password))
            .thenReturn(encodedPassword)
        
        // 直接返回預先創建的 savedUser 物件
        Mockito.`when`(userRepository.save(any())).thenAnswer{ _ -> savedUser }
        
        // 執行測試
        val result = userService.registerUser(registerRequest)
        
        // 驗證結果
        assert(result == savedUser.id)
        
        // 驗證模擬物件被正確調用
        verify(userRepository).findByAccount(registerRequest.account)
        verify(userRepository).findByEmail(registerRequest.email)
        verify(passwordEncoder).encode(registerRequest.password)
        verify(userRepository).save(any())
    }
    
    @Test
    fun `當帳號已存在時應拋出衝突錯誤`() {
        // 設置測試數據
        val registerRequest = RegisterRequest(
            account = "existingUser",
            email = "new@example.com",
            username = "Existing User",
            password = "password123"
        )
        
        val existingUser = User(
            id = UUID.randomUUID(),
            account = registerRequest.account,
            email = "existing@example.com",
            username = "Existing User",
            password = "encodedPassword",
            role = UserRole.USER
        )
        
        // 設置模擬行為 - 帳號已存在
        whenever(userRepository.findByAccount(registerRequest.account))
            .thenReturn(Optional.of(existingUser))
        
        // 執行測試並驗證異常
        val exception = assertThrows<ResponseStatusException> {
            userService.registerUser(registerRequest)
        }
        
        // 驗證錯誤訊息和狀態碼
        assert(exception.statusCode == HttpStatus.CONFLICT)
        assert(exception.reason == "Account already exists")
        
        // 驗證只調用了查詢帳號的方法
        verify(userRepository).findByAccount(registerRequest.account)
        verify(userRepository, never()).findByEmail(any())
        verify(userRepository, never()).save(any())
    }
    
    @Test
    fun `當信箱已存在時應拋出衝突錯誤`() {
        // 設置測試數據
        val registerRequest = RegisterRequest(
            account = "newUser",
            email = "existing@example.com",
            username = "New User",
            password = "password123"
        )
        
        val existingUser = User(
            id = UUID.randomUUID(),
            account = "existingUser",
            email = registerRequest.email,
            username = "Existing User",
            password = "encodedPassword",
            role = UserRole.USER
        )
        
        // 設置模擬行為 - 帳號不存在但信箱已存在
        whenever(userRepository.findByAccount(registerRequest.account))
            .thenReturn(Optional.empty())
        whenever(userRepository.findByEmail(registerRequest.email))
            .thenReturn(Optional.of(existingUser))
        
        // 執行測試並驗證異常
        val exception = assertThrows<ResponseStatusException> {
            userService.registerUser(registerRequest)
        }
        
        // 驗證錯誤訊息和狀態碼
        assert(exception.statusCode == HttpStatus.CONFLICT)
        assert(exception.reason == "Email already exists")
        
        // 驗證調用了查詢帳號和信箱的方法，但沒有保存用戶
        verify(userRepository).findByAccount(registerRequest.account)
        verify(userRepository).findByEmail(registerRequest.email)
        verify(userRepository, never()).save(any())
    }
    
    @Test
    fun `當使用者儲存失敗時應拋出內部伺服器錯誤`() {
        // 設置測試數據
        val registerRequest = RegisterRequest(
            account = "newUser",
            email = "new@example.com",
            username = "New User",
            password = "password123"
        )
        
        val encodedPassword = "encodedPassword"
        // 創建一個 id 為 null 的使用者，模擬保存失敗的情況
        val savedUser = User(
            id = null, // ID 為 null，模擬保存失敗
            account = registerRequest.account,
            email = registerRequest.email,
            username = registerRequest.username,
            password = encodedPassword,
            role = UserRole.USER
        )
        
        // 設置模擬行為
        whenever(userRepository.findByAccount(registerRequest.account))
            .thenReturn(Optional.empty())
        whenever(userRepository.findByEmail(registerRequest.email))
            .thenReturn(Optional.empty())
        whenever(passwordEncoder.encode(registerRequest.password))
            .thenReturn(encodedPassword)
        Mockito.`when`(userRepository.save(any())).thenThrow(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user"))

        // 執行測試並驗證異常
        val exception = assertThrows<ResponseStatusException> {
            userService.registerUser(registerRequest)
        }
        
        // 驗證錯誤訊息和狀態碼
        assert(exception.statusCode == HttpStatus.INTERNAL_SERVER_ERROR)
        assert(exception.reason == "Failed to create user")
    }
}