package tech.igrant.jizhang.detail

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface DetailRepo : CrudRepository<Detail, Long> {

    @Query(value = "select new tech.igrant.jizhang.detail.DetailVo (" +
            "d.id, u.username as username, a1.name as sourceAccountName, a2.name as destAccountName, " +
            "sub.name as subjectName, " +
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

}