package tech.igrant.jizhang.account

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Long> {

    @Query(nativeQuery = true, value = "select * from account where user_id = :userId")
    fun findByUserId(@Value("userId") userId: Long): List<Account>

}