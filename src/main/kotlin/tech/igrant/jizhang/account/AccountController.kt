package tech.igrant.jizhang.account

import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.user.UserRepo

@RestController
@RequestMapping("/api/accounts")
class AccountController(
        private val accountRepo: AccountRepo,
        private val userRepo: UserRepo
) {

    @GetMapping()
    fun list(): List<AccountVo> {
        return accountRepo.listWithUsername().toList()
    }

    @PostMapping()
    fun create(@RequestBody account: Account): AccountVo {
        val userObject = userRepo.findById(account.userId).get()
        accountRepo.save(account)
        return AccountVo.fromAccount(account, userObject.username)
    }

}