package tech.igrant.jizhang.fix

import org.apache.commons.lang3.time.DateUtils
import org.junit.Assert
import org.junit.Test
import tech.igrant.jizhang.ext.FMT_YYYY_MM_dd_HH_mm_ss
import tech.igrant.jizhang.ext.fmt

class MonthFixedExpenseGeneratorTest {

    @Test
    fun apply() {
        val fixedExpenses = FixedExpenses(
                fixedForm = FixedForm.MONTH,
                indexInPeriod = 5,
                start = DateUtils.parseDate("2020-01-01 00:00:00", FMT_YYYY_MM_dd_HH_mm_ss),
                end = DateUtils.parseDate("2020-05-30 23:59:59", FMT_YYYY_MM_dd_HH_mm_ss),
                generated = FixedExpenses.TO_GENERATE,
                generateType = GenerateType.ALL,
                remark = "",
                userId = 1,
                sourceAccountId = 1,
                destAccountId = 1,
                subjectId = 1,
                amount = 500
        )
        val details = MonthFixedExpenseGenerator().apply(fixedExpenses)
        Assert.assertEquals(5, details.size)
        val expected = arrayOf(
                "2020-01-05 00:00:00",
                "2020-02-05 00:00:00",
                "2020-03-05 00:00:00",
                "2020-04-05 00:00:00",
                "2020-05-05 00:00:00"
        )
        details.forEachIndexed { index, detail ->
            Assert.assertEquals(expected[index], detail.createdAt.fmt(FMT_YYYY_MM_dd_HH_mm_ss))
        }
    }
}