package tech.igrant.jizhang.detail

import io.swagger.annotations.ApiOperation
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.account.AccountRepo
import tech.igrant.jizhang.subject.SubjectRepo
import tech.igrant.jizhang.user.UserRepo

@RestController
@RequestMapping("/api/details")
class DetailController(
        private val userRepo: UserRepo,
        private val subjectRepo: SubjectRepo,
        private val accountRepo: AccountRepo,
        private val detailRepo: DetailRepo
) {

    @ApiOperation("分页列出明细")
    @GetMapping
    fun list(pageable: Pageable): Page<DetailVo> {
        return detailRepo.listVo(pageable = pageable)
    }

    @ApiOperation("删除单个明细")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long) {
        return detailRepo.deleteById(id)
    }

    @ApiOperation("增加一条明细")
    @PostMapping
    fun create(@RequestBody detail: Detail): DetailVo {
        val user = userRepo.findById(detail.userId).get()
        val subject = subjectRepo.findById(detail.subjectId).get()
        val accountNameMap = accountRepo.findAllById(
                listOf(detail.sourceAccountId, detail.destAccountId)
        ).associateBy({ a -> a.id }, { a -> a.name })
        detailRepo.save(detail)
        return DetailVo.fromPo(
                detail,
                username = user.username,
                sourceAccountName = accountNameMap[detail.sourceAccountId].orEmpty(),
                destAccountName = accountNameMap[detail.destAccountId].orEmpty(),
                subjectName = subject.name
        )
    }

    @ApiOperation("更新一条明细")
    @PutMapping
    @ResponseBody
    fun update(@RequestBody detail: DetailUpdateTo): ResponseEntity<DetailVo> {
        val detailInDbOpt = detailRepo.findById(detail.id)
        if (detailInDbOpt.isPresent) {
            val detailInDb = detailInDbOpt.get()
            val user = userRepo.findById(detail.userId).get()
            val subject = subjectRepo.findById(detail.subjectId).get()
            val accountNameMap = accountRepo.findAllById(
                    listOf(detail.sourceAccountId, detail.destAccountId)
            ).associateBy({ a -> a.id }, { a -> a.name })
            BeanUtils.copyProperties(detail, detailInDb)
            detailRepo.save(detailInDb)
            return ResponseEntity.ok(DetailVo.fromPo(
                    detailInDb,
                    username = user.username,
                    sourceAccountName = accountNameMap[detail.sourceAccountId].orEmpty(),
                    destAccountName = accountNameMap[detail.destAccountId].orEmpty(),
                    subjectName = subject.name
            ))
        } else {
            return ResponseEntity.notFound().build()
        }
    }
}