package tech.igrant.jizhang.user

import org.springframework.data.repository.CrudRepository

interface UserRepo : CrudRepository<User, Long> {
    fun findByAccount(account: String): User?
}