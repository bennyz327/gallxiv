package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.UserRole
import com.benjk.gallexiv.data.dto.ChangePasswordRequest
import com.benjk.gallexiv.data.dto.RegisterRequest
import com.benjk.gallexiv.data.dto.UpdateUserRequest
import com.benjk.gallexiv.data.entity.User
import com.benjk.gallexiv.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    fun findById(id: UUID): User = userRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }

    fun findByAccountPassword(account: String, password: String): User = userRepository
        .findByAccount(account)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
        .also {
            if (!passwordEncoder.matches(password, it.password)) {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password")
            }
        }
    
    /**
     * 註冊新使用者
     * 檢查帳號和信箱是否已存在，並加密密碼
     *
     * @param registerRequest 註冊資訊
     * @return 新建的使用者 ID
     * @throws ResponseStatusException 若帳號或信箱已存在
     */
    @Transactional
    fun registerUser(registerRequest: RegisterRequest): UUID {
        // 檢查帳號是否已存在
        userRepository.findByAccount(registerRequest.account).ifPresent {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Account already exists")
        }
        
        // 檢查信箱是否已存在
        userRepository.findByEmail(registerRequest.email).ifPresent {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Email already exists")
        }
        
        // 建立新使用者
        val user = User(
            account = registerRequest.account,
            email = registerRequest.email,
            username = registerRequest.username,
            password = passwordEncoder.encode(registerRequest.password),
            role = UserRole.USER
        )
        
        // 儲存使用者並返回 ID
        return userRepository.save(user).id ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user")
    }

    /**
     * 更改用戶密碼
     * 驗證當前密碼並更新為新密碼
     *
     * @param user 當前用戶
     * @param request 密碼修改請求
     * @throws ResponseStatusException 若當前密碼不正確
     */
    @Transactional
    fun changePassword(user: User, request: ChangePasswordRequest) {
        // 驗證當前密碼
        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current password is incorrect")
        }
        
        // 檢查新密碼是否與當前密碼相同
        if (passwordEncoder.matches(request.newPassword, user.password)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different from current password")
        }
        
        // 更新密碼
        user.password = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }

    /**
     * 更新使用者資訊
     *
     * @param user 當前用戶
     * @param request 更新使用者資訊請求
     * @throws ResponseStatusException 若使用者不存在
     */
    @Transactional
    fun updateUser(user: User, request: UpdateUserRequest) {
        user.username = request.username
        userRepository.save(user)
    }
}
