package tech.igrant.jizhang.account

import javax.persistence.*

@Entity
@Table(name = "account")
class Account(
        var type: String,
        var userId: Long = -1,
        var name: String,
        var description: String,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

class AccountVo(
        var id: Long,
        var type: String,
        var name: String,
        var description: String,
        var username: String
) {
    companion object {
        fun fromAccount(account: Account, username: String): AccountVo {
            return AccountVo(
                    id = account.id!!,
                    type = account.type,
                    name = account.name,
                    description = account.description,
                    username = username
            )
        }
    }
}