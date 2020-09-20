package tech.igrant.jizhang.fix

import org.springframework.stereotype.Component

interface FixedExpenseService {

    fun create(fixedExpenseTo: FixedExpenseTo): FixedExpenseVo

    fun generate(id: Long): Void

    @Component
    class Impl : FixedExpenseService {

        override fun create(fixedExpenseTo: FixedExpenseTo): FixedExpenseVo {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun generate(id: Long): Void {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}