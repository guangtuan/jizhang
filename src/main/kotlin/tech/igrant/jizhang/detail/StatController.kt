package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@RestController
@RequestMapping("/api/stats")
class StatController(private val detailRepo: DetailRepo) {

    @GetMapping()
    fun query(): List<StatDetail> {
        val query = detailRepo.query(
                Date.from(Instant.now().minus(30, ChronoUnit.DAYS)),
                Date.from(Instant.now())
        )
        query.forEach { println(it) }
        return query
    }

}