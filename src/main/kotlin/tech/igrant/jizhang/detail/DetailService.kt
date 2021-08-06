package tech.igrant.jizhang.detail

import org.springframework.stereotype.Service
import tech.igrant.jizhang.account.Account
import tech.igrant.jizhang.account.AccountService
import tech.igrant.jizhang.detail.it.BY
import tech.igrant.jizhang.detail.it.ItDetailQuery
import tech.igrant.jizhang.event.EventService
import tech.igrant.jizhang.ext.toJSON
import tech.igrant.jizhang.ext.toLocalDateTime
import tech.igrant.jizhang.framework.PageQuery
import tech.igrant.jizhang.framework.PageResult
import tech.igrant.jizhang.subject.Subject
import tech.igrant.jizhang.subject.SubjectService
import tech.igrant.jizhang.user.User
import tech.igrant.jizhang.user.UserService
import java.time.LocalDateTime
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
    private val detailRepo: DetailRepo,
    private val eventService: EventService
) {

    val logger: Logger = Logger.getLogger(DetailService::class.simpleName)

    fun query(detailQuery: PageQuery<DetailQuery>): PageResult<DetailVo> {
        logger.info { "params: ${detailQuery.toJSON()}" }
        val startTime = System.nanoTime()
        val criteriaBuilder = entityManager.criteriaBuilder
        val rowsQuery = criteriaBuilder.createQuery(Detail::class.java)
        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val data = rowsQuery.from(Detail::class.java)
        val count = countQuery.from(Detail::class.java)
        val createdAtColumn = data.get<LocalDateTime>("createdAt")
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
        val subjectMap = subjectService.subjectMap()
        val accountMap = accountService.lookup()
        val userMap = userService.userMap()
        val content = resultList.map { this.toVo(it, subjectMap, accountMap, userMap) }.toList()
        val eventMap = eventService.eventMap(content.map { detailVo -> detailVo.id })
        logger.info("eventMap: ${eventMap.toJSON()}")
        for (detailVo in content) {
            eventMap[detailVo.id]?.let {
                detailVo.eventId = it.id
                detailVo.eventName = it.name
            }
        }
        val pageResult = PageResult(
            content = content,
            total = total,
            page = detailQuery.page,
            size = detailQuery.size
        )
        val endTime = System.nanoTime()
        val duration = (endTime - startTime) / 1000000
        logger.info("execute: $duration ms")
        return pageResult
    }

    private fun conditions(
        builder: CriteriaBuilder,
        detailQuery: PageQuery<DetailQuery>,
        data: Root<Detail>
    ): Predicate {
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
                val createdAtRow = data.get<LocalDateTime>("createdAt")
                conditions.add(
                    builder.between(
                        createdAtRow,
                        detailQuery.queryParam.start.toLocalDateTime(),
                        detailQuery.queryParam.end.toLocalDateTime()
                    )
                )
            }
        }
        return conditions.reduce { acc, curr -> builder.and(acc, curr) }
    }

    fun toVo(
        detail: Detail,
        subjectMap: Map<Long, Subject>,
        accountMap: Map<Long, Account>,
        userMap: Map<Long, User>
    ): DetailVo {
        val vo = DetailVo.fromPo(detail, "", "", "", "", null)
        detail.sourceAccountId?.let {
            vo.sourceAccountName = accountMap[it]?.name
        }
        detail.destAccountId?.let {
            vo.destAccountName = accountMap[it]?.name
        }
        vo.subjectName = subjectMap[vo.subjectId]?.name
        vo.username = userMap[detail.userId]?.nickname
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

    fun itQuery(q: ItDetailQuery): List<Detail> {
        if (q.by == BY.SUBJECT) {
            return detailRepo.findBySubjectIdAndTime(q.ids, Date(q.start), Date(q.end))
        }

        return emptyList()
    }

}