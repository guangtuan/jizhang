package tech.igrant.jizhang.accountState

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class AccountState(
        var amount: Int,
        var accountId: Long = -1,
        var createdAt: Date,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

class AccountStateVo(
        var id: Long,
        var amount: Int,
        var createdAt: Date,
        var accountName: String
) {
    companion object {
        fun fromAccountState(accountState: AccountState, accountName: String): AccountStateVo {
            return AccountStateVo(
                    id = accountState.id!!,
                    accountName = accountName,
                    createdAt = accountState.createdAt,
                    amount = accountState.amount
            )
        }
    }
}