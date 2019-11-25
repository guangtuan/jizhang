package tech.igrant.jizhang.subject

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(private val subjectRepo: SubjectRepo) {

    @GetMapping()
    fun list(pageable: Pageable): Page<Subject> {
        return subjectRepo.findAll(pageable)
    }

    @PostMapping
    fun create(@RequestBody subject: Subject): Subject {
        return subjectRepo.save(subject)
    }

}