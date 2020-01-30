package tech.igrant.jizhang.detail

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface DetailRepo : CrudRepository<Detail, Long> {

    @Query(value = "select new tech.igrant.jizhang.detail.DetailVo (" +
            "d.id, " +
            "u.id as userId, u.username as username, " +
            "a1.id as sourceAccountId, a1.name as sourceAccountName, " +
            "a2.id as destAccountId, a2.name as destAccountName, " +
            "sub.id as subjectId, sub.name as subjectName, " +
            "d.remark, d.createdAt, d.updatedAt, d.amount) " +
            "from Detail d " +
            "left join User u " +
            "on d.userId = u.id " +
            "left join Account a1 " +
            "on d.sourceAccountId = a1.id " +
            "left join Account a2 " +
            "on d.destAccountId = a2.id " +
            "left join User u " +
            "on d.userId = u.id " +
            "left join Subject sub " +
            "on d.subjectId = sub.id " +
            "order by created_at desc")
    fun listVo(pageable: Pageable): Page<DetailVo>

    fun findBySourceAccountId(accountId: Long): List<Detail>
    fun findByDestAccountId(accountId: Long): List<Detail>

    @Query(value = "select new tech.igrant.jizhang.detail.AmountTotal (d.subjectId, sub.name as subjectName, (sum(d.amount)/100) as total) " +
            "from Detail d left join Subject sub on d.subjectId = sub.id " +
            "where d.destAccountId = -1 " +
            "and d.createdAt between :start and :end " +
            "and d.subjectId in (:subjectIds) " +
            "group by d.subjectId " +
            "order by total")
    fun query(
            @Param("start") start: Date,
            @Param("end") end: Date,
            @Param("subjectIds") subjectIds: List<Long>
    ): List<AmountTotal>
}