package tech.igrant.jizhang.subject

import io.swagger.annotations.ApiOperation
import org.apache.commons.lang3.time.DateUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.detail.Detail
import tech.igrant.jizhang.detail.DetailService
import tech.igrant.jizhang.detail.it.BY
import tech.igrant.jizhang.detail.it.ItDetailQuery
import tech.igrant.jizhang.ext.fmt
import tech.igrant.jizhang.ext.toDate
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/api/subjects")
@ApiOperation("科目接口")
class SubjectController(
    private val subjectRepo: SubjectRepo,
    private val detailService: DetailService,
    private val subjectService: SubjectService
) {

    @ApiOperation("统计科目的曲线")
    @PostMapping("/stat")
    fun stat(
        @RequestBody subjectStatQueryRequest: SubjectStatQueryRequest
    ): ResponseEntity<List<SubjectCost>> {
        val details = getDetails(
            subjectStatQueryRequest.ids,
            subjectStatQueryRequest.level,
            subjectStatQueryRequest.start,
            subjectStatQueryRequest.end
        )
        val map = details
            .groupBy { d -> d.subjectId }
            .map { (key, value) ->
                value.groupBy { d -> getYearMonth(d) }.entries.map { e ->
                    SubjectCost(
                        subjectName = subjectRepo.findById(key).get().name,
                        unit = StatUnit.Month,
                        display = e.key,
                        cost = e.value.map { v -> v.amount.toLong() / 100 }.reduce { acc, i -> acc + i })
                }.sortedBy { subjectCost -> DateUtils.parseDate(subjectCost.display, "yyyy-MM") }
            }
            .flatten()
        return ResponseEntity.of(Optional.of(map))
    }

    private fun getDetails(ids: List<Long>, level: Int, start: Long, end: Long): List<Detail> {
        if (level == Subject.LEVEL_SMALL) {
            return detailService.itQuery(ItDetailQuery(BY.SUBJECT, ids, start, end))
        }
        val childrenIds = subjectService.flat(ids).mapNotNull { sub -> sub.id }
        return detailService.itQuery(ItDetailQuery(BY.SUBJECT, childrenIds, start, end))
    }

    private fun getYearMonth(d: Detail): String {
        return d.createdAt.toDate().fmt("yyyy-MM")
    }

    @ApiOperation("列出科目")
    @GetMapping()
    fun list(): List<SubjectVo> {
        val others = -1L
        val parents = subjectRepo.findParent().map { po -> po.toVo(null) }.toMutableList()
        val childrenGroupedByParent = subjectRepo.findChildren().groupBy { po -> po.parentId ?: others }
        parents.add(
            SubjectVo(
                id = others,
                name = "其他",
                description = "其他",
                children = mutableListOf(),
                parentId = null,
                parent = null,
                level = 1,
                createdAt = LocalDateTime.now()
            )
        )
        for (parent in parents) {
            childrenGroupedByParent[parent.id]?.let {
                parent.children.addAll(it.map { child -> child.toVo(parent.name) })
            }
        }
        return parents
    }

    @ApiOperation("根据等级拿到科目")
    @GetMapping(params = ["by=level"])
    fun listByLevel(
        @RequestParam("level", required = true) level: Int,
        @RequestParam("parentId", required = false) parentId: Long?
    ): ResponseEntity<List<SubjectVo>> {
        return when (level) {
            1 -> ResponseEntity.ok(subjectRepo.findParent().map { po -> po.toVo(null) }.toList())
            2 -> parentId?.let {
                ResponseEntity.ok(subjectRepo.findChildrenByParent(it).map { po -> po.toVo(null) }.toList())
            }
                ?: ResponseEntity.ok(subjectRepo.findChildren().map { po -> po.toVo(null) }.toList())
            else -> ResponseEntity.badRequest().build()
        }
    }

    @ApiOperation("新建一个科目")
    @PostMapping
    fun create(@RequestBody subjectTo: SubjectTo): SubjectVo {
        return subjectRepo.save(subjectTo.toPo(null)).toVo(null)
    }

    @ApiOperation("更新一个科目")
    @PutMapping("{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody subjectTo: SubjectTo): ResponseEntity<SubjectVo?> {
        return subjectRepo.findById(id)
            .map { ResponseEntity.ok(subjectRepo.save(subjectTo.toPo(it)).toVo(null)) }
            .orElse(ResponseEntity.notFound().build())
    }

    @ApiOperation("删除一个科目")
    @DeleteMapping("{id}")
    fun del(@PathVariable("id") id: Long): ResponseEntity<Any> {
        val subject = subjectRepo.findById(id)
        return subject.map {
            when (it.level) {
                Subject.LEVEL_BIG -> {
                    val childSubjects = subjectRepo.findChildrenByParent(it.id!!)
                    if (childSubjects.isEmpty()) {
                        subjectRepo.deleteById(id)
                        ResponseEntity.ok().build<Any>()
                    } else {
                        ResponseEntity.status(HttpStatus.CONFLICT).build<Any>()
                    }
                }
                else -> {
                    val detailsWithSubject = detailService.getBySubjectId(it.id!!)
                    if (detailsWithSubject.isEmpty()) {
                        subjectRepo.deleteById(id)
                        ResponseEntity.ok().build<Any>()
                    } else {
                        ResponseEntity.status(HttpStatus.CONFLICT).build<Any>()
                    }
                }
            }
        }.orElse(ResponseEntity.notFound().build<Any>())
    }

}