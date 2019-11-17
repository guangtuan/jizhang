package tech.igrant.jizhang.accountState

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.account.AccountRepo

@RestController
@RequestMapping("/api/accountStates")
class AccountStateController(
        private val accountRepo: AccountRepo,
        private val accountStateRepo: AccountStateRepo
) {

    @GetMapping
    @ResponseBody
    fun list(pageable: Pageable): Page<AccountStateVo> {
        return accountStateRepo.listVo(pageable = pageable)
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