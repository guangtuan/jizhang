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
        return if (amountTotalQuery.subjects.isEmpty()) {
            detailRepo.query(
                    amountTotalQuery.start,
                    amountTotalQuery.end.getStartOfTomorrow(),
                    subjectIds = subjectRepo.findAll().mapNotNull { sub -> sub.id }
            )
        } else {
            detailRepo.query(
                    amountTotalQuery.start,
                    amountTotalQuery.end.getStartOfTomorrow(),
                    amountTotalQuery.subjects
            )
        }

    }

}