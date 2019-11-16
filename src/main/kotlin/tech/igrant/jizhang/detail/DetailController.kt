package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.account.AccountRepo
import tech.igrant.jizhang.subject.SubjectRepo
import tech.igrant.jizhang.user.UserRepo
import java.util.*

@RestController
@RequestMapping("/api/details")
class DetailController(
        private val userRepo: UserRepo,
        private val subjectRepo: SubjectRepo,
        private val accountRepo: AccountRepo,
        private val detailRepo: DetailRepo
) {

    @GetMapping
    fun list(): List<DetailVo> {
        return detailRepo.listVo().toList()
    }

    @PostMapping
    fun create(@RequestBody detail: Detail): DetailVo {
        val user = userRepo.findById(detail.userId).get()
        val subject = subjectRepo.findById(detail.subjectId).get()
        val accountNameMap = accountRepo.findAllById(
                listOf(detail.sourceAccountId, detail.destAccountId)
        ).associateBy({ a -> a.id }, { a -> a.name })
        detail.createdAt = Date()
        detailRepo.save(detail)
        return DetailVo.fromPo(
                detail,
                username = user.username,
                sourceAccountName = accountNameMap[detail.sourceAccountId].orEmpty(),
                destAccountName = accountNameMap[detail.destAccountId].orEmpty(),
                subjectName = subject.name
        )
    }

}