package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/stats")
class StatController(private val detailRepo: DetailRepo) {

    @GetMapping()
    fun query(
            @RequestParam("start") start: Long,
            @RequestParam("end") end: Long
    ): List<StatDetail> {
        return detailRepo.query(
                Date(start),
                Date(end)
        )
    }

}