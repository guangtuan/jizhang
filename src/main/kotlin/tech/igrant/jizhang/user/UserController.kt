package tech.igrant.jizhang.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepo: UserRepo) {

    @GetMapping("/{user}")
    fun user(@PathVariable("user") account: String): UserTo? {
        val user = userRepo.findByAccount(account)
        user?.let {
            return UserTo.fromUser(user)
        }
        return null
    }

    @PostMapping
    fun users(): List<UserTo?> {
        return userRepo.findAll().toList().map { UserTo.fromUser(it) }
    }

}