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
    fun userMap(userIds: List<Long>): Map<Long, User>
    fun userMap(): Map<Long, User>

    @Service
    class Impl(
        private val userRepo: UserRepo,
        private val sessionRepo: SessionRepo
    ) : UserService {

        override fun userMap(userIds: List<Long>): Map<Long, User> {
            return userRepo.findAllById(userIds).fold(mutableMapOf()) { acc, user ->
                user.id?.let {
                    acc[it] = user
                }
                acc
            }
        }

        override fun userMap(): Map<Long, User> {
            return userRepo.findAll().fold(mutableMapOf()) { acc, user ->
                user.id?.let {
                    acc[it] = user
                }
                acc
            }
        }

        override fun createUser(to: UserTo): User {
            val initialPassword = "123456"
            val salt = randomString()
            val password = PasswordEncrypt.encrypt(initialPassword, salt)
            return userRepo.save(
                User(
                    email = to.email,
                    avatar = to.avatar,
                    nickname = to.nickname,
                    password = password,
                    salt = salt
                )
            )
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
                    sessionRepo.getToken(loginForm.email)?.let { tokenExists ->
                        return SessionBody(
                            userId = user.id!!,
                            email = user.email,
                            token = tokenExists,
                            nickname = user.nickname
                        )
                    }
                    sessionRepo.save(it.email, token)
                    return SessionBody(
                        userId = user.id!!,
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