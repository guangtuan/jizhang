package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.igrant.jizhang.ext.getStartOfTomorrow
import tech.igrant.jizhang.subject.SubjectRepo

@RestController
@RequestMapping("/api/stats")
class StatController(
    private val detailRepo: DetailRepo,
    private val subjectRepo: SubjectRepo
) {

    @PostMapping()
    fun query(@RequestBody amountTotalQuery: AmountTotalQuery): List<AmountTotal> {
        return detailRepo
            .findByStartAndEnd(amountTotalQuery.start, amountTotalQuery.end.getStartOfTomorrow())
            .filter { d -> d.destAccountId == null }
            .filter { d: Detail -> amountTotalQuery.subjects.isEmpty() || amountTotalQuery.subjects.contains(d.subjectId) }
            .groupBy { detail: Detail -> detail.subjectId }
            .entries.map {
                AmountTotal(
                    it.key,
                    subjectRepo.findById(it.key).map { sub -> sub.name }.orElse("未知"),
                    it.value.map { v -> v.amount.toLong() }.reduce { acc, i -> acc + i } / 100
                )
            }
            .sortedBy { i -> i.total }
            .toList()
    }

}