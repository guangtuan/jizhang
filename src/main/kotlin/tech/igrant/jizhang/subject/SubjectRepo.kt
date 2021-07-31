package tech.igrant.jizhang.subject

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface SubjectRepo : CrudRepository<Subject, Long> {

    @Query(nativeQuery = true, value = "select * from subject where level = 1 order by created_at desc")
    fun findParent(): List<Subject>

    @Query(nativeQuery = true, value = "select * from subject where level = 2 order by created_at desc")
    fun findChildren(): List<Subject>

    @Query(
        nativeQuery = true,
        value = "select * from subject where level = 2 and parent_id = :parentId order by created_at desc"
    )
    fun findChildrenByParent(@Param("parentId") parentId: Long): List<Subject>

    @Query(
        nativeQuery = true,
        value = "select * from subject where level = 2 and parent_id in :parentIds order by created_at desc"
    )
    fun findChildrenByParents(@Param("parentIds") parentIds: List<Long>): List<Subject>

}