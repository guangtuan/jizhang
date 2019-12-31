package tech.igrant.jizhang.user

import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepo: UserRepo) {

    @PostMapping("/{user}")
    fun user(@PathVariable("user") account: String): UserTo? {
        val user = userRepo.findByAccount(account)
        user?.let {
            return UserTo.display(user)
        }
        return null
    }

    @ApiOperation("列出所有的用户")
    @GetMapping()
    fun users(): List<UserTo?> {
        return userRepo.findAll().toList().map { UserTo.display(it) }
    }

    @ApiOperation("新建一个用户")
    @PostMapping()
    fun createUser(@RequestBody userTo: UserTo): UserTo {
        val userToSave = UserTo.dbFormat(userTo)
        return UserTo.display(userRepo.save(userToSave))
    }

}