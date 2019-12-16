package tech.igrant.jizhang.user

import tech.igrant.jizhang.PasswordEncrypt
import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        var account: String,
        var password: String,
        var salt: String,
        var username: String,
        var avatar: String?,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

fun randomString(len: Int = 16): String {
    val sources = ('0'..'z').toList().toTypedArray()
    return (1..len).map { sources.random() }.joinToString("")
}

class UserTo(
        var account: String,
        var username: String,
        var id: Long?,
        var avatar: String?
) {
    companion object {
        fun dbFormat(to: UserTo): User {
            val initialPassword = "123456"
            val salt = randomString()
            val password = PasswordEncrypt.encrypt(initialPassword, salt)
            return User(
                    account = to.account,
                    avatar = to.avatar,
                    username = to.username,
                    password = password,
                    salt = salt
            )
        }

        fun display(user: User): UserTo {
            return UserTo(
                    id = user.id,
                    avatar = user.avatar,
                    account = user.account,
                    username = user.username
            )
        }
    }
}