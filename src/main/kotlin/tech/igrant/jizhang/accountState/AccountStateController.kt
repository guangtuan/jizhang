package tech.igrant.jizhang.accountState

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accountStates")
class AccountStateController(private val accountStateRepo: AccountStateRepo) {

    @GetMapping
    fun list(): List<AccountState> {
        return accountStateRepo.findAll().toList()
    }

    @PostMapping
    fun create(@RequestBody accountState: AccountState): AccountState {
        return accountStateRepo.save(accountState)
    }

}