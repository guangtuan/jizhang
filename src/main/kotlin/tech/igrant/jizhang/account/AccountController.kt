package tech.igrant.jizhang.account

import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.user.UserRepo

@RestController
@RequestMapping("/api/accounts")
class AccountController(
        private val accountRepo: AccountRepo,
        private val userRepo: UserRepo
) {

    @ApiOperation("列出所有账户")
    @GetMapping()
    fun list(): List<AccountVo> {
        return accountRepo.listWithUsername().toList()
    }

    @ApiOperation("新建一个账户")
    @PostMapping()
    fun create(@RequestBody account: Account): AccountVo {
        val userObject = userRepo.findById(account.userId).get()
        accountRepo.save(account)
        return AccountVo.fromAccount(account, userObject.username)
    }

}