package tech.igrant.jizhang.subject

import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.igrant.jizhang.detail.DetailService
import java.util.*

@RestController
@RequestMapping("/api/subjects")
@ApiOperation("科目接口")
class SubjectController(
        private val subjectRepo: SubjectRepo,
        private val detailService: DetailService
) {

    @ApiOperation("列出科目")
    @GetMapping()
    fun list(): List<SubjectVo> {
        val others = -1L
        val parents = subjectRepo.findParent().map { po -> po.toVo(null) }.toMutableList()
        val childrenGroupedByParent = subjectRepo.findChildren().groupBy { po -> po.parentId ?: others }
        parents.add(SubjectVo(id = others, name = "其他", description = "其他", children = mutableListOf(), parentId = null, parent = null, level = 1, createdAt = Date()))
        for (parent in parents) {
            childrenGroupedByParent[parent.id]?.let {
                parent.children.addAll(it.map { child -> child.toVo(parent.name) })
            }
        }
        return parents
    }

    @ApiOperation("根据等级拿到科目")
    @GetMapping(params = ["by=level"])
    fun listByLevel(@RequestParam("level", required = true) level: Int, @RequestParam("parentId", required = false) parentId: Long?): ResponseEntity<List<SubjectVo>> {
        return when (level) {
            1 -> ResponseEntity.ok(subjectRepo.findParent().map { po -> po.toVo(null) }.toList())
            2 -> parentId?.let { ResponseEntity.ok(subjectRepo.findChildrenByParent(it).map { po -> po.toVo(null) }.toList()) }
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