package tech.igrant.jizhang.detail

import com.google.common.collect.Maps
import org.springframework.stereotype.Service
import tech.igrant.jizhang.ext.getStartOfTomorrow
import tech.igrant.jizhang.subject.SubjectService
import java.util.*

interface StatService {

    fun statByBigSubject(start: Date, end: Date, subjectIds: List<Long>): List<AmountTotal>

    fun statBySmallSubject(start: Date, end: Date, subjectIds: List<Long>): List<AmountTotal>

    @Service
    class Impl(
        private val detailRepo: DetailRepo,
        private val subjectService: SubjectService
    ) : StatService {

        private fun listRequire(start: Date, end: Date): List<Detail> {
            return detailRepo
                .findByStartAndEnd(start, end.getStartOfTomorrow())
                .filter { d -> d.destAccountId == null }
        }

        override fun statBySmallSubject(start: Date, end: Date, subjectIds: List<Long>): List<AmountTotal> {
            val subjectNameLookup = Maps.transformValues(subjectService.subjectMap()) { v -> v?.name }
            return listRequire(start, end)
                .filter { d -> subjectIds.isEmpty() || d.subjectId in subjectIds }
                .groupBy { d -> d.subjectId }
                .entries.map {
                    AmountTotal(
                        it.key,
                        subjectNameLookup.getOrDefault(it.key, "未知"),
                        it.value.map { v -> v.amount.toLong() }.reduce { acc, i -> acc + i } / 100
                    )
                }
                .sortedBy { i -> i.total }
                .toList()
        }

        override fun statByBigSubject(start: Date, end: Date, subjectIds: List<Long>): List<AmountTotal> {
            val subjectLookup = subjectService.subjectMap()
            val subjectNameLookup = Maps.transformValues(subjectLookup) { v -> v?.name }
            return listRequire(start, end)
                .filter { d ->
                    subjectIds.isEmpty() || subjectLookup[d.subjectId]?.parentId in subjectIds
                }
                .groupBy { d -> subjectLookup[d.subjectId]?.parentId }
                .entries.mapNotNull {
                    it.key?.let { key ->
                        AmountTotal(
                            key,
                            subjectNameLookup.getOrDefault(it.key, "未知"),
                            it.value.map { v -> v.amount.toLong() }.reduce { acc, i -> acc + i } / 100
                        )
                    }
                }
                .sortedBy { i -> i.total }
                .toList()
        }
    }

}