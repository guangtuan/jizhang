package tech.igrant.jizhang.subject

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface SubjectRepo : CrudRepository<Subject, Long> {

    fun findAll(pageable: Pageable): Page<Subject>

}