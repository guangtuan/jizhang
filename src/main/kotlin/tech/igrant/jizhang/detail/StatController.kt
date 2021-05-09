package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.igrant.jizhang.subject.Subject

@RestController
@RequestMapping("/api/stats")
class StatController(
    private val statService: StatService
) {

    @PostMapping()
    fun query(@RequestBody amountTotalQuery: AmountTotalQuery): List<AmountTotal> {
        return when (amountTotalQuery.level) {
            Subject.LEVEL_BIG -> statService.statByBigSubject(
                amountTotalQuery.start,
                amountTotalQuery.end,
                amountTotalQuery.subjects
            )

            Subject.LEVEL_SMALL -> statService.statBySmallSubject(
                amountTotalQuery.start,
                amountTotalQuery.end,
                amountTotalQuery.subjects
            )
            else -> emptyList()
        }

    }

}