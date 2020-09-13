package tech.igrant.jizhang.fix

interface FixedExpenseService {

    fun create(fixedExpenseTo: FixedExpenseTo): FixedExpenseVo

    fun generate(id: Long): Void

}