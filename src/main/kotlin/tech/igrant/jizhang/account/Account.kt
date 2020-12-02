package tech.igrant.jizhang.account

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "account")
data class Account(
        var type: String,
        var userId: Long = -1,
        var name: String,
        var description: String,
        var createdAt: Date,
        var updatedAt: Date,
        var initAmount: Int,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

data class AccountTo(
        var userId: Long = -1,
        var type: String,
        var name: String,
        var description: String,
        var initAmount: Int
) {

    fun toAccount(): Account {
        return Account(
                type = this.type,
                userId = this.userId,
                name = this.name,
                description = this.description,
                createdAt = Date(),
                updatedAt = Date(),
                initAmount = this.initAmount
        )
    }

}

data class AccountVo(
        var id: Long,
        var type: String,
        var name: String,
        var userId: Long,
        var createdAt: Date,
        var updatedAt: Date,
        var description: String,
        var nickname: String,
        var initAmount: Int
) {
    companion object {
        fun fromAccount(account: Account, nickname: String): AccountVo {
            return AccountVo(
                    id = account.id!!,
                    userId = account.userId,
                    type = account.type,
                    name = account.name,
                    description = account.description,
                    nickname = nickname,
                    createdAt = account.createdAt,
                    updatedAt = account.updatedAt,
                    initAmount = account.initAmount
            )
        }
    }
}