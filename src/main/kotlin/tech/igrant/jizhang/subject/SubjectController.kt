package tech.igrant.jizhang.subject

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(private val subjectRepo: SubjectRepo) {

    @GetMapping()
    fun list(): List<Subject> {
        return subjectRepo.findAll().toList()
    }

    @PostMapping
    fun create(@RequestBody subject: Subject): Subject {
        return subjectRepo.save(subject)
    }

}