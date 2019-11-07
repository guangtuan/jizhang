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