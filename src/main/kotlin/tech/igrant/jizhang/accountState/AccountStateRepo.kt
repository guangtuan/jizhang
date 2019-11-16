package tech.igrant.jizhang.accountState

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AccountStateRepo : CrudRepository<AccountState, Long> {

    @Query(value = "select new tech.igrant.jizhang.accountState.AccountStateVo(s.id, s.amount, s.createdAt, a.name as accountName) " +
            "from AccountState s " +
            "left join Account a " +
            "on s.accountId = a.id")
    fun listVo(): List<AccountStateVo>

}