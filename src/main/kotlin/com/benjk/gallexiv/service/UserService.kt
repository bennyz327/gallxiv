package com.benjk.gallexiv.service

import com.benjk.gallexiv.data.UserRole
import com.benjk.gallexiv.data.dto.RegisterRequest
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
}