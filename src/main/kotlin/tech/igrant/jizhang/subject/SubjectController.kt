package tech.igrant.jizhang.subject

import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
@ApiOperation("科目接口")
class SubjectController(
        private val subjectRepo: SubjectRepo
) {

    @ApiOperation("列出科目")
    @GetMapping()
    fun list(): List<SubjectVo> {
        val others = -1L
        val list = subjectRepo.findParent().map { po -> po.toVo() }.toMutableList()
        val groupBy = subjectRepo.findChildren().map { po -> po.toVo() }.groupBy { po -> po.parentId ?: others }
        list.add(SubjectVo(id = others, name = "其他", description = "其他", children = mutableListOf(), parentId = null))
        for (subjectVo in list) {
            groupBy[subjectVo.id]?.let {
                subjectVo.children.addAll(it)
            }
        }
        return list
    }

    @ApiOperation("根据登记拿到科目")
    @GetMapping(params = ["by=level"])
    fun listByLevel(@RequestParam("level", required = true) level: Int, @RequestParam("parentId", required = false) parentId: Long?): ResponseEntity<List<SubjectVo>> {
        return when (level) {
            1 -> ResponseEntity.ok(subjectRepo.findParent().map { po -> po.toVo() }.toList())
            2 -> parentId?.let { ResponseEntity.ok(subjectRepo.findChildrenByParent(it).map { po -> po.toVo() }.toList()) }
                    ?: ResponseEntity.ok(subjectRepo.findChildren().map { po -> po.toVo() }.toList())
            else -> ResponseEntity.badRequest().build()
        }
    }

    @ApiOperation("新建一个科目")
    @PostMapping
    fun create(@RequestBody subjectTo: SubjectTo): Subject {
        return subjectRepo.save(subjectTo.toPo())
    }

}