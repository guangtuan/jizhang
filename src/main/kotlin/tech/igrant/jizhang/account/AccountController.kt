package tech.igrant.jizhang.account

import io.swagger.annotations.ApiOperation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.detail.DetailService
import tech.igrant.jizhang.user.UserRepo
import tech.igrant.jizhang.user.UserService
import java.util.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val detailService: DetailService,
    private val userService: UserService,
    private val accountRepo: AccountRepo,
    private val userRepo: UserRepo
) {

    val logger: Logger = LoggerFactory.getLogger(AccountController::class.java)

    @ApiOperation("列出所有账户")
    @GetMapping
    fun list(): List<AccountVo> {
        val accounts = accountRepo.findAll()
        val userIds = accounts.map { account -> account.userId }.toSet().toList()
        val userMap = userService.userMap(userIds)
        return accounts.mapNotNull { account ->
            userMap[account.userId]?.let {
                AccountVo.fromAccount(account, it.nickname)
            }
        }
    }

    @ApiOperation("根据用户列出账户")
    @GetMapping(params = ["by=user"])
    fun listByUser(userId: Long): List<AccountVo> {
        val nickname = userService.userMap(listOf(userId))[userId]?.nickname ?: ""
        return accountRepo.findByUserId(userId)
            .map { account -> AccountVo.fromAccount(account, nickname) }
    }

    @ApiOperation("新建一个账户")
    @PostMapping()
    fun create(@RequestBody accountTo: AccountTo): AccountVo {
        val userObject = userRepo.findById(accountTo.userId).get()
        return AccountVo.fromAccount(
            accountRepo.save(accountTo.toAccount()),
            userObject.nickname
        )
    }

    @ApiOperation("更新一个账户")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody accountTo: AccountTo): ResponseEntity<AccountVo?> {
        return accountRepo.findById(id)
            .map {
                logger.info("acc: {}", it)
                val userObject = userRepo.findById(accountTo.userId).get()
                it.userId = accountTo.userId
                it.type = accountTo.type
                it.name = accountTo.name
                it.description = accountTo.description
                it.tail = accountTo.tail
                it.updatedAt = Date()
                ResponseEntity.ok(
                    AccountVo.fromAccount(
                        accountRepo.save(it),
                        userObject.nickname
                    )
                )
            }
            .orElse(ResponseEntity.notFound().build())
    }

    @ApiOperation("删除一个账户，已经有明细了则不允许删除账户")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Any> {
        val details = detailService.getByAccountId(id)
        if (!details.isNullOrEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        accountRepo.deleteById(id)
        return ResponseEntity.noContent().build();
    }

}