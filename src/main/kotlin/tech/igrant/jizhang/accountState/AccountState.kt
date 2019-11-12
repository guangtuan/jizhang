package tech.igrant.jizhang.accountState

import java.util.*
import javax.persistence.*

@Entity
class AccountState(
        var amount: Int,
        var account: String,
        var createdAt: Date,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)