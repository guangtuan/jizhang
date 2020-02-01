package tech.igrant.jizhang.detail

import org.springframework.stereotype.Service
import tech.igrant.jizhang.account.AccountService
import tech.igrant.jizhang.subject.SubjectService
import tech.igrant.jizhang.user.UserService
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Root

@Service
class DetailService(
        private val entityManager: EntityManager,
        private val accountService: AccountService,
        private val subjectService: SubjectService,
        private val userService: UserService
) {

    fun listBySubject(detailQuery: DetailQuery): List<DetailVo> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Detail::class.java)
        val root = criteriaQuery.from(Detail::class.java)
        val subjectIdsFilter = subjectIdsFilter(criteriaBuilder, root, detailQuery)
        val createdAtColumn = root.get<Any>("createdAt")
        val sql = criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.asc(createdAtColumn))
        subjectIdsFilter?.let { sql.where(it) }
        return entityManager.createQuery(sql)
                .setFirstResult(detailQuery.page * detailQuery.size)
                .setMaxResults((detailQuery.page + 1) * detailQuery.size)
                .resultList
                .map(this::toVo)
                .toList()
    }

    private fun subjectIdsFilter(criteriaBuilder: CriteriaBuilder, root: Root<Detail>, detailQuery: DetailQuery): CriteriaBuilder.In<Any>? {
        if (detailQuery.subjectIds.isEmpty()) {
            return null
        }
        val subjectIdColumn = root.get<Any>("subjectId")
        val inExpression = criteriaBuilder.`in`(subjectIdColumn)
        detailQuery.subjectIds.forEach { i -> inExpression.value(i) }
        return inExpression
    }

    fun toVo(detail: Detail): DetailVo {
        val vo = DetailVo.fromPo(detail, "", "", "", "")
        detail.sourceAccountId?.let {
            vo.sourceAccountName = accountService.findById(it)?.name
        }
        detail.destAccountId?.let {
            vo.destAccountName = accountService.findById(it)?.name
        }
        vo.subjectName = subjectService.findById(vo.subjectId)?.name
        vo.username = userService.findById(detail.userId)?.username
        return vo
    }

}