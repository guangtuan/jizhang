package tech.igrant.jizhang.settlement

import org.springframework.stereotype.Service
import tech.igrant.jizhang.account.AccountRepo
import tech.igrant.jizhang.account.AccountType
import tech.igrant.jizhang.accountState.AccountState
import tech.igrant.jizhang.accountState.AccountStateRepo
import tech.igrant.jizhang.detail.Detail
import tech.igrant.jizhang.detail.DetailRepo
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
}

@Service
class SettlementService(
        private val accountRepo: AccountRepo,
        private val accountStateRepo: AccountStateRepo,
        private val detailRepo: DetailRepo
) {

    fun work(accountId: Long): Settlement {
        val accountOptional = accountRepo.findById(accountId)
        if (!accountOptional.isPresent) {
            throw IllegalArgumentException("no account record for $accountId")
        }
        val account = accountOptional.get()
        val accountStates = accountStateRepo.findByAccountId(accountId)
        val dateStringComparator = kotlin.Comparator<String> { o1, o2 -> LocalDate.parse(o1).compareTo(LocalDate.parse(o2)) }
        val sysSettleLookup = accountStates
                .associateBy({ a -> a.createdAt.toString() }, { a -> a })
                .toSortedMap(dateStringComparator)
        val dateStrings = sysSettleLookup.keys.toList().sortedWith(dateStringComparator)
        val detailSourceLookup = detailRepo.findBySourceAccountId(accountId)
        val detailDestLookup = detailRepo.findByDestAccountId(accountId)
        val records = mutableListOf<Record>()
        for ((idx, dateString) in dateStrings.withIndex()) {
            records.plus(
                    createRecord(idx, dateString, dateStrings, sysSettleLookup, detailSourceLookup, detailDestLookup)
            )
        }
        return Settlement(
                accountId = accountId,
                accountType = AccountType.valueOf(account.type),
                accountName = account.name,
                records = records
        )
    }

    private fun createRecord(
            idx: Int,
            d: String,
            dateStrings: List<String>,
            sysSettleLookup: SortedMap<String, AccountState>,
            asSourceRecords: List<Detail>,
            asDestRecords: List<Detail>
    ): Record {
        val start: Date = LocalDate.parse(d).toDate()
        val end: Date = if (idx + 1 == dateStrings.size) {
            // end
            LocalDate.now().toDate()
        } else {
            LocalDate.parse(dateStrings[idx + 1]).toDate()
        }

        val accountState = sysSettleLookup.get(d)

        val settleAsSource = asSourceRecords
                .filter { detail -> detail.createdAt!!.before(end) && detail.createdAt!!.after(start) }
                .fold(accountState?.amount) { acc, detail -> acc?.plus(detail.amount) }
        val asDestSettle = asDestRecords
                .filter { detail -> detail.createdAt!!.before(end) && detail.createdAt!!.after(start) }
                .fold(accountState?.amount) { acc, detail -> acc?.plus(detail.amount) }
        return Record(
                start = start,
                end = end,
                sysSettle = 0.0,
                userSettle = 0.0
        )
    }

}