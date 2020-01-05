package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.igrant.jizhang.ext.getStartOfTomorrow

@RestController
@RequestMapping("/api/stats")
class StatController(private val detailRepo: DetailRepo) {

    @PostMapping()
    fun query(@RequestBody amountTotalQuery: AmountTotalQuery): List<AmountTotal> {
        return detailRepo.query(
                amountTotalQuery.start,
                amountTotalQuery.end.getStartOfTomorrow(),
                amountTotalQuery.subjects
        )
    }

}