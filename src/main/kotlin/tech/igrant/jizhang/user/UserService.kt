package tech.igrant.jizhang.user

import org.springframework.stereotype.Service
import tech.igrant.jizhang.PasswordEncrypt
import tech.igrant.jizhang.ext.randomString
import tech.igrant.jizhang.session.LoginForm
import tech.igrant.jizhang.session.SessionBody
import tech.igrant.jizhang.session.SessionRepo
import java.util.*

interface UserService {

    fun createUser(to: UserTo): User
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun login(loginForm: LoginForm): SessionBody?

    @Service
    class Impl(
            private val userRepo: UserRepo,
            private val sessionRepo: SessionRepo
    ) : UserService {

        override fun createUser(to: UserTo): User {
            val initialPassword = "123456"
            val salt = randomString()
            val password = PasswordEncrypt.encrypt(initialPassword, salt)
            return userRepo.save(User(
                    email = to.email,
                    avatar = to.avatar,
                    nickname = to.nickname,
                    password = password,
                    salt = salt
            ))
        }

        override fun findByEmail(email: String): User? {
            return userRepo.findByEmail(email)
        }

        override fun findById(id: Long): User? {
            val opt = userRepo.findById(id)
            if (opt.isPresent) {
                return opt.get()
            }
            return null
        }

        override fun login(loginForm: LoginForm): SessionBody? {
            val user = userRepo.findByEmail(loginForm.email)
            user?.let {
                if (PasswordEncrypt.valid(loginForm.password, it.password, it.salt)) {
                    val token = UUID.randomUUID().toString().replace("-", "")
                    sessionRepo.save(it.email, token)
                    return SessionBody(
                            email = user.email,
                            token = token,
                            nickname = user.nickname
                    )
                }
            }
            return null
        }
    }

}