package tech.igrant.jizhang.user

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepo: UserRepo) {

    @PostMapping("/query/{user}")
    fun user(@PathVariable("user") account: String): UserTo? {
        val user = userRepo.findByAccount(account)
        user?.let {
            return UserTo.display(user)
        }
        return null
    }

    @PostMapping("/query")
    fun users(): List<UserTo?> {
        return userRepo.findAll().toList().map { UserTo.display(it) }
    }

    @PostMapping()
    fun createUser(@RequestBody userTo: UserTo): UserTo {
        val userToSave = UserTo.dbFormat(userTo)
        return UserTo.display(userRepo.save(userToSave))
    }

}