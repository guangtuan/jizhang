package tech.igrant.jizhang.user

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class UserInit(private val userService: UserService) {

    val logger = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    fun init() {
        val adminUsername = "admin"
        val adminEmail = "admin"
        val user = userService.findByEmail(adminEmail)
        user?.let {
            logger.info("please login with username: {}, password: {}", adminUsername, "123456")
        } ?: {
            val to = UserTo(
                    email = adminEmail,
                    nickname = adminUsername,
                    id = null,
                    avatar = null
            )
            userService.createUser(to)
            logger.info("please login with adminEmail: {}, password: {}", adminEmail, "123456")
        }()
    }

}