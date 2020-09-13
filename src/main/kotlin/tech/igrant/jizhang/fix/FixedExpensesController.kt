package tech.igrant.jizhang.fix

import io.swagger.annotations.ApiOperation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ApiOperation("新建一个固定支出，会保存一个记录，选择「生成」，会生成对应的明细")
@RestController("api/v1/fixedExpense")
class FixedExpensesController(
        private val fixedExpenseService: FixedExpenseService
) {

    @PostMapping()
    @ResponseBody
    fun create(fixedExpenseTo: FixedExpenseTo): ResponseEntity<FixedExpenseVo> {
        return ResponseEntity.ok(fixedExpenseService.create(fixedExpenseTo))
    }

    @PutMapping("/{id}/generate")
    @ResponseBody
    fun generate(@PathVariable("id") id: Long): ResponseEntity<Any> {
        fixedExpenseService.generate(id)
        return ResponseEntity.ok().build()
    }

}