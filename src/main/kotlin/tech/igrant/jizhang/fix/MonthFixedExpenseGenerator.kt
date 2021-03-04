package tech.igrant.jizhang.fix

import tech.igrant.jizhang.detail.Detail
import tech.igrant.jizhang.ext.fmt
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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
            val createdAt = LocalDateTime.of(startLocalDate.plusMonths(it.toLong()), LocalTime.of(0, 0, 0))
            Detail(
                    userId = fixedExpenses.userId,
                    sourceAccountId = fixedExpenses.sourceAccountId,
                    destAccountId = fixedExpenses.destAccountId,
                    createdAt = createdAt,
                    amount = fixedExpenses.amount,
                    remark = fixedExpenses.remark,
                    subjectId = fixedExpenses.subjectId,
                    updatedAt = createdAt,
                    splited = Detail.NOT_SPLITED,
                    parentId = null
            )
        }
    }

}