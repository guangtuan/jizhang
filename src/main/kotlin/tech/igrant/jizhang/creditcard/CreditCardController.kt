package tech.igrant.jizhang.creditcard

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/api/creditCards")
class CreditCardController(
        private val creditCardService: CreditCardService
) {

    @PostMapping("")
    @ResponseBody
    fun create(@RequestBody request: CreditCardCreateRequest): ResponseEntity<CreditCardVo> {
        return creditCardService.create(request)
                .map { ResponseEntity.accepted().body(it) }
                .orElse(ResponseEntity.badRequest().build())
    }

    @GetMapping("")
    @ResponseBody
    fun list(): ResponseEntity<List<CreditCardVo>> {
        return ResponseEntity.ok(creditCardService.loadAll())
    }

    @PutMapping("{id}")
    @ResponseBody
    fun update(@PathVariable("id") id: Long, @RequestBody request: CreditCardUpdateRequest): ResponseEntity<CreditCardVo> {
        return creditCardService.update(id, request)
                .map { ResponseEntity.accepted().body(it) }
                .orElse(ResponseEntity.badRequest().build())
    }

}