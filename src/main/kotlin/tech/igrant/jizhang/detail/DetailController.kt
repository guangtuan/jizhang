package tech.igrant.jizhang.detail

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/details")
class DetailController(private val detailRepo: DetailRepo) {

    @GetMapping
    fun list(): List<Detail> {
        return detailRepo.findAll().toList()
    }

    @PostMapping
    fun create(@RequestBody detail: Detail): Detail {
        detail.createdAt = Date()
        return detailRepo.save(detail)
    }

}