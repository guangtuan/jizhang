package tech.igrant.jizhang.user

import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepo: UserRepo, private val userService: UserService) {

    @PostMapping("/{user}")
    fun user(@PathVariable("user") email: String): UserTo? {
        val user = userRepo.findByEmail(email)
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
        return UserTo.display(userService.createUser(userTo))
    }

}