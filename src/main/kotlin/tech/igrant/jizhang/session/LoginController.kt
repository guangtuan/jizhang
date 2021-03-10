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
    fun login(@RequestBody loginForm: LoginForm): ResponseEntity<Any> {
        val token = userService.login(loginForm)
        token?.let {
            return ResponseEntity.ok(it)
        }
        return ResponseEntity.badRequest().body("错误的用户名或者密码")
    }

}

@ApiModel
data class LoginForm(
        @ApiModelProperty("邮箱")
        val email: String,
        @ApiModelProperty("密码")
        val password: String
)

@ApiModel
data class SessionBody(
        @ApiModelProperty("用户id")
        val userId: Long,
        @ApiModelProperty("email，后续请求的时候放在 http header 里")
        val email: String,
        @ApiModelProperty("token，后续请求的时候放在 http header 里")
        val token: String,
        @ApiModelProperty("用户名")
        val nickname: String
)