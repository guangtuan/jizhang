package tech.igrant.jizhang.account

import org.springframework.data.repository.CrudRepository

interface AccountRepo : CrudRepository<Account, Long> {
    fun findByName(name: String): Account?
}