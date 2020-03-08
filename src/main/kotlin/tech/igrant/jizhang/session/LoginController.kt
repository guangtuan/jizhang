package tech.igrant.jizhang.session

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import tech.igrant.jizhang.user.UserService

@RestController
class LoginController(private val userService: UserService) {

    @PostMapping("login")
    fun login(@RequestBody loginForm: LoginForm): ResponseEntity<SessionBody> {
        val token = userService.login(loginForm)
        token?.let {
            return ResponseEntity.ok(SessionBody(it))
        }
        return ResponseEntity.badRequest().build()
    }

}

@ApiModel
data class LoginForm (
        @ApiModelProperty("用户名")
        val username: String,
        @ApiModelProperty("密码")
        val password: String
)

@ApiModel
data class SessionBody (
        @ApiModelProperty("token，请求的时候放在 http header 里")
        val token: String
)