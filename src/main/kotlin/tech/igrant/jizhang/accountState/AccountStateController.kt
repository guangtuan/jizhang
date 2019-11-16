package tech.igrant.jizhang.accountState

import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.account.AccountRepo

@RestController
@RequestMapping("/api/accountStates")
class AccountStateController(
        private val accountRepo: AccountRepo,
        private val accountStateRepo: AccountStateRepo
) {

    @GetMapping
    fun list(): List<AccountStateVo> {
        return accountStateRepo.listVo().toList()
    }

    @PostMapping
    fun create(@RequestBody accountState: AccountState): AccountStateVo {
        val optional = accountRepo.findById(accountState.accountId)
        if (optional.isPresent) {
            val account = optional.get()
            accountStateRepo.save(accountState)
            return AccountStateVo.fromAccountState(accountState, accountName = account.name)
        } else {
            throw Exception("该账户不存在")
        }
    }

}