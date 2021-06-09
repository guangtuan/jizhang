package tech.igrant.jizhang.detail

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface DetailRepo : JpaRepository<Detail, Long>,
    JpaSpecificationExecutor<Detail> {

    fun findBySourceAccountId(accountId: Long): List<Detail>
    fun findByDestAccountId(accountId: Long): List<Detail>
    fun findBySubjectId(subjectId: Long): List<Detail>

    @Query(value = "select * from detail where created_at between :start and :end", nativeQuery = true)
    fun findByStartAndEnd(@Param("start") start: Date, @Param("end") end: Date): List<Detail>

}