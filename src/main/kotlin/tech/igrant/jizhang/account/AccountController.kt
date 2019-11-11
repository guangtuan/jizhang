package tech.igrant.jizhang.account

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val accountRepo: AccountRepo) {

    @GetMapping()
    fun list(): List<Account> {
        return accountRepo.findAll().toList()
    }

    @PostMapping()
    fun create(@RequestBody account: Account): Account {
        accountRepo.save(account)
        return account
    }

}