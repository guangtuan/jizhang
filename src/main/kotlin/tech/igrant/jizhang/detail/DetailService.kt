package tech.igrant.jizhang.detail

import org.springframework.stereotype.Service
import tech.igrant.jizhang.account.AccountService
import tech.igrant.jizhang.ext.toJSON
import tech.igrant.jizhang.framework.PageQuery
import tech.igrant.jizhang.framework.PageResult
import tech.igrant.jizhang.subject.SubjectService
import tech.igrant.jizhang.user.UserService
import java.util.*
import java.util.logging.Logger
import javax.persistence.EntityManager
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

@Service
class DetailService(
        private val entityManager: EntityManager,
        private val accountService: AccountService,
        private val subjectService: SubjectService,
        private val userService: UserService,
        private val detailRepo: DetailRepo
) {

    val logger: Logger = Logger.getLogger(DetailService::class.simpleName)

    fun query(detailQuery: PageQuery<DetailQuery>): PageResult<DetailVo> {
        logger.info { "params: ${detailQuery.toJSON()}" }
        val criteriaBuilder = entityManager.criteriaBuilder
        val rowsQuery = criteriaBuilder.createQuery(Detail::class.java)
        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val data = rowsQuery.from(Detail::class.java)
        val count = countQuery.from(Detail::class.java)
        val createdAtColumn = data.get<Any>("createdAt")
        val dataSql = rowsQuery.select(data).orderBy(criteriaBuilder.desc(createdAtColumn))
        val countSql = countQuery.select(criteriaBuilder.count(count))
        if (DetailQuery.meaningful(detailQuery.queryParam)) {
            val condition = conditions(criteriaBuilder, detailQuery, data)
            dataSql.where(condition)
            countSql.where(condition)
        }
        val startIndex = detailQuery.page * detailQuery.size
        val createQuery = entityManager.createQuery(dataSql)
        if (detailQuery.size != -1) {
            createQuery.setFirstResult(startIndex).maxResults = detailQuery.size
        }
        val resultList = createQuery.resultList
        val total = entityManager.createQuery(countSql).singleResult
        return PageResult(
                content = resultList.map(this::toVo).toList(),
                total = total,
                page = detailQuery.page,
                size = detailQuery.size
        )
    }

    private fun conditions(builder: CriteriaBuilder, detailQuery: PageQuery<DetailQuery>, data: Root<Detail>): Predicate {
        val conditions = mutableListOf<Predicate>()
        detailQuery.queryParam.subjectIds?.let {
            if (it.isNotEmpty()) {
                val subjectIdRow = data.get<Long>("subjectId")
                val inExpression = builder.`in`(subjectIdRow)
                for (subjectId in it) {
                    inExpression.value(subjectId)
                }
                conditions.add(inExpression)
            }
        }
        detailQuery.queryParam.sourceAccountId?.let {
            val sourceAccountIdRow = data.get<Long>("sourceAccountId")
            conditions.add(builder.equal(sourceAccountIdRow, it))
        }
        detailQuery.queryParam.destAccountId?.let {
            val destAccountIdRow = data.get<Long>("destAccountId")
            conditions.add(builder.equal(destAccountIdRow, it))
        }
        detailQuery.queryParam.start?.let {
            detailQuery.queryParam.end?.let {
                val createdAtRow = data.get<Date>("createdAt")
                conditions.add(builder.between(createdAtRow, detailQuery.queryParam.start, detailQuery.queryParam.end))
            }
        }
        return conditions.reduce { acc, curr -> builder.and(acc, curr) }
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
        vo.username = userService.findById(detail.userId)?.nickname
        return vo
    }

    fun getByAccountId(id: Long): List<Detail> {
        return listOf(
                detailRepo.findByDestAccountId(id),
                detailRepo.findBySourceAccountId(id)
        ).flatten()
    }

    fun getBySubjectId(subjectId: Long): List<Detail> {
        return detailRepo.findBySubjectId(subjectId);
    }

}