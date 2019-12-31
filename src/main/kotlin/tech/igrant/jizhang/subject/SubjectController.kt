package tech.igrant.jizhang.subject

import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
@ApiOperation("科目接口")
class SubjectController(private val subjectRepo: SubjectRepo) {

    @ApiOperation("分页列出科目")
    @GetMapping()
    fun list(pageable: Pageable): Page<Subject> {
        return subjectRepo.findAll(pageable)
    }

    @ApiOperation("新建一个科目")
    @PostMapping
    fun create(@RequestBody subject: Subject): Subject {
        return subjectRepo.save(subject)
    }

}