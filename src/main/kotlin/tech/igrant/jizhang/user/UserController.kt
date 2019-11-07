package tech.igrant.jizhang.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/user")
@RestController
class UserController(private val userRepo: UserRepo) {

    @GetMapping("/{user}")
    fun user(@PathVariable("user") account: String): User? {
        return userRepo.findByAccount(account);
    }

}