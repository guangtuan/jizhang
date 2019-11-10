package tech.igrant.jizhang.user

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
        var account: String,
        var password: String,
        var salt: String,
        var username: String,
        var avatar: String,
        @Id @GeneratedValue var id: Long? = null
)

class UserTo(
        var account: String,
        var username: String,
        var id: Long?,
        var avatar: String
) {
    companion object {
        fun fromUser(user: User): UserTo {
            return UserTo(
                    id = user.id,
                    avatar = user.avatar,
                    account = user.account,
                    username = user.username
            )
        }
    }
}