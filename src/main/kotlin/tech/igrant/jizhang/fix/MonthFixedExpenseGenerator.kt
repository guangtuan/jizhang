package tech.igrant.jizhang.fix

import tech.igrant.jizhang.detail.Detail
import tech.igrant.jizhang.ext.fmt
import tech.igrant.jizhang.ext.toDate
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@CanHandleFixedForm(FixedForm.MONTH)
class MonthFixedExpenseGenerator : FixedExpenseGenerator {

    override fun apply(fixedExpenses: FixedExpenses): List<Detail> {
        val startLocalDate = LocalDate.parse(fixedExpenses.start.fmt("YYYY-MM-dd")).withDayOfMonth(fixedExpenses.indexInPeriod)
        val monthCount = ChronoUnit.MONTHS.between(
                startLocalDate,
                LocalDate.parse(fixedExpenses.end.fmt("YYYY-MM-dd")).withDayOfMonth(fixedExpenses.indexInPeriod)
        )
        return IntRange(0, monthCount.toInt()).map {
            val createdAt = startLocalDate.plusMonths(it.toLong()).toDate()
            Detail(
                    userId = fixedExpenses.userId,
                    sourceAccountId = fixedExpenses.sourceAccountId,
                    destAccountId = fixedExpenses.destAccountId,
                    createdAt = createdAt,
                    amount = fixedExpenses.amount,
                    remark = fixedExpenses.remark,
                    subjectId = fixedExpenses.subjectId,
                    updatedAt = createdAt
            )
        }
    }

}