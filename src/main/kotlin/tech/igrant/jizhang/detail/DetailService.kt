package tech.igrant.jizhang.detail

import org.springframework.stereotype.Service
import tech.igrant.jizhang.account.AccountService
import tech.igrant.jizhang.framework.PageQuery
import tech.igrant.jizhang.framework.PageResult
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

    fun listBySubject(detailQuery: PageQuery<DetailQuery>): PageResult<DetailVo> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val rowsQuery = criteriaBuilder.createQuery(Detail::class.java)
        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val data = rowsQuery.from(Detail::class.java)
        val count = countQuery.from(Detail::class.java)
        val subjectIdsFilter = subjectIdsFilter(criteriaBuilder, data, detailQuery.queryParam)
        val createdAtColumn = data.get<Any>("createdAt")
        val dataSql = rowsQuery.select(data).orderBy(criteriaBuilder.asc(createdAtColumn))
        val countSql = countQuery.select(criteriaBuilder.count(count))
        subjectIdsFilter?.let {
            dataSql.where(it);
            countSql.where(it)
        }
        val dataQueryExe = entityManager.createQuery(dataSql)
        val countQueryExe = entityManager.createQuery(countSql)
        val resultList = dataQueryExe
                .setFirstResult(detailQuery.page * detailQuery.size)
                .setMaxResults((detailQuery.page + 1) * detailQuery.size)
                .resultList
        val total = countQueryExe.singleResult
        return PageResult(
                content = resultList.map(this::toVo).toList(),
                total = total,
                page = detailQuery.page,
                size = detailQuery.size
        )
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