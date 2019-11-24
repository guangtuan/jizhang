package tech.igrant.jizhang.settlement

import tech.igrant.jizhang.account.AccountType
import java.util.*

class Settlement(
        var accountId: Long,
        var accountName: String,
        var accountType: AccountType,
        var records: List<Record>
)

data class Record(
        var start: Date,
        var end: Date,
        var userSettle: Double,
        var sysSettle: Double
)