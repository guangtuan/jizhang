package tech.igrant.jizhang.fix

import tech.igrant.jizhang.detail.Detail

interface FixedExpenseGenerator {

    fun apply(fixedExpenses: FixedExpenses): List<Detail>

}