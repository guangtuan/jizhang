package tech.igrant.jizhang.user

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import tech.igrant.jizhang.PasswordEncrypt
import tech.igrant.jizhang.session.LoginForm
import java.util.*

@Service
class UserService(
        private val userRepo: UserRepo,
        private val redisTemplate: StringRedisTemplate
) {

    fun findById(id: Long): User? {
        val opt = userRepo.findById(id)
        if (opt.isPresent) {
            return opt.get()
        }
        return null
    }

    fun login(loginForm: LoginForm): String? {
        val findByAccount = userRepo.findByAccount(loginForm.username)
        findByAccount?.let {
            if (PasswordEncrypt.valid(loginForm.password, it.password, it.salt)) {
                val token = UUID.randomUUID().toString().replace("-", "")
                redisTemplate.opsForValue().set("token:" + it.account, token)
                return token
            }
        }
        return null
    }

}