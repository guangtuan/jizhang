package tech.igrant.jizhang.subject

import io.swagger.annotations.ApiOperation
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
        return subjectRepo.findAll().toList()
                .fold(
                        mutableMapOf<Long, SubjectVo>(),
                        { acc, po ->
                            po.parentId?.let {
                                acc[it]?.children?.add(po.toVo())
                            }
                            if (!acc.containsKey(po.id)) {
                                po.toVo().apply {
                                    acc[this.id] = this
                                }
                            }
                            acc
                        }
                )
                .values
                .filter { s -> s.parentId == null }
                .toList()
    }

    @ApiOperation("新建一个科目")
    @PostMapping
    fun create(@RequestBody subjectTo: SubjectTo): Subject {
        return subjectRepo.save(subjectTo.toPo())
    }

}