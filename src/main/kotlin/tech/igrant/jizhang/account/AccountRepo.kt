package tech.igrant.jizhang.account

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Long> {
    @Query(value = "select new tech.igrant.jizhang.account.AccountVo " +
            "(a.id, a.type, a.name as name, a.description, u.nickname) " +
            "from Account a left join User u on a.userId = u.id")
    fun listWithUsername(): List<AccountVo>

}