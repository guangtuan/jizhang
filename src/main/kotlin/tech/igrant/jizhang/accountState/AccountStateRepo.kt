package tech.igrant.jizhang.accountState

import org.springframework.data.repository.CrudRepository

interface AccountStateRepo : CrudRepository<AccountState, Long> {}