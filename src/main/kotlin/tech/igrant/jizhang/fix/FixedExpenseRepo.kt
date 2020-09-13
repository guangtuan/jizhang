package tech.igrant.jizhang.fix

import org.springframework.data.repository.CrudRepository

interface FixedExpenseRepo: CrudRepository<FixedExpenses, Long> {



}