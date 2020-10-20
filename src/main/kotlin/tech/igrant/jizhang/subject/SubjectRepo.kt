package tech.igrant.jizhang.subject

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface SubjectRepo : CrudRepository<Subject, Long> {

    @Query(nativeQuery = true, value = "select * from subject where level = 1")
    fun findParent(): List<Subject>

    @Query(nativeQuery = true, value = "select * from subject where level = 2")
    fun findChildren(): List<Subject>

    @Query(nativeQuery = true, value = "select * from subject where level = 2 and parent_id = :parentId")
    fun findChildrenByParent(@Param("parentId") parentId: Long): List<Subject>

}