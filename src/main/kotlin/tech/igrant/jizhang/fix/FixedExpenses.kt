package tech.igrant.jizhang.fix

import io.swagger.annotations.ApiModel
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

enum class FixedForm {
    DAY, WEEK, MONTH, YEAR
}

enum class GenerateType {
    REAL_TIME, ALL
}

@Entity
class FixedExpenses(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private val id: Long? = null,
        val fixedForm: FixedForm,
        val indexInPeriod: Int,
        val start: Date,
        val end: Date,
        val generated: Int,
        val generateType: GenerateType,
        var remark: String?,
        var userId: Long,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        val amount: Int
) {
    companion object {
        const val GENERATED = 1
        const val TO_GENERATE = 2
    }

}

@ApiModel("新建固定支出所传的对象")
data class FixedExpenseTo(
        val fixedForm: FixedForm,
        val start: Date,
        val end: Date,
        val indexInPeriod: Int,
        var remark: String? = null,
        var userId: Long,
        val generateType: GenerateType,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        val amount: Int
)

@ApiModel("新建固定支出返回的对象")
data class FixedExpenseVo(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private val id: Long? = null,
        val fixedForm: FixedForm,
        val indexInPeriod: Int,
        val start: Date,
        val end: Date,
        val generated: Int,
        val generateType: GenerateType,
        var remark: String?,
        var userId: Long,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        val amount: Int
)