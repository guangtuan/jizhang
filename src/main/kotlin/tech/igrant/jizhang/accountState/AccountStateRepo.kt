package tech.igrant.jizhang.accountState

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

interface AccountStateRepo : PagingAndSortingRepository<AccountState, Long> {

    @Query(value = "select new tech.igrant.jizhang.accountState.AccountStateVo(s.id, s.amount, s.createdAt, a.name as accountName) " +
            "from AccountState s " +
            "left join Account a " +
            "on s.accountId = a.id")
    fun listVo(pageable: Pageable): Page<AccountStateVo>

}