package tech.igrant.jizhang.user

import javax.persistence.*

@Entity
@Table(name = "user")
data class User(
        var email: String,
        var password: String,
        var salt: String,
        var nickname: String,
        var avatar: String?,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

class UserTo(
        var email: String,
        var nickname: String,
        var id: Long?,
        var avatar: String?
) {
    companion object {
        fun display(user: User): UserTo {
            return UserTo(
                    id = user.id,
                    avatar = user.avatar,
                    email = user.email,
                    nickname = user.nickname
            )
        }
    }
}