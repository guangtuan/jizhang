package tech.igrant.jizhang.detail

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Detail(
        var userId: Long,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        var remark: String?,
        var createdAt: Date?,
        var updatedAt: Date?,
        var amount: Int,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

@ApiModel("明细更新所传的对象")
class DetailUpdateTo(
        @ApiModelProperty("用户id")
        val userId: Long,
        @ApiModelProperty("来源账户id，如用支付宝吃饭，来源账户就是支付宝")
        val sourceAccountId: Long? = null,
        @ApiModelProperty("目标账户id，如发工资的时候，目标账户就是工资卡")
        val destAccountId: Long? = null,
        @ApiModelProperty("科目id")
        val subjectId: Long,
        @ApiModelProperty("备注")
        val remark: String? = null,
        @ApiModelProperty("金额，单位分")
        val amount: Int? = null
)

@ApiModel("明细供视图使用")
data class DetailVo(
        var id: Long,
        @ApiModelProperty("用户id")
        val userId: Long,
        var username: String?,
        @ApiModelProperty("来源账户id，如用支付宝吃饭，来源账户就是支付宝")
        val sourceAccountId: Long? = null,
        var sourceAccountName: String?,
        @ApiModelProperty("目标账户id，如发工资的时候，目标账户就是工资卡")
        val destAccountId: Long? = null,
        var destAccountName: String?,
        @ApiModelProperty("科目id")
        val subjectId: Long,
        var subjectName: String?,
        var remark: String?,
        var createdAt: Date?,
        var updatedAt: Date?,
        var amount: Int
) {
    companion object {
        fun fromPo(
                po: Detail,
                username: String,
                sourceAccountName: String,
                destAccountName: String,
                subjectName: String
        ): DetailVo {
            return DetailVo(
                    id = po.id!!,
                    username = username,
                    sourceAccountName = sourceAccountName,
                    destAccountName = destAccountName,
                    subjectName = subjectName,
                    remark = po.remark,
                    createdAt = po.createdAt,
                    updatedAt = po.updatedAt,
                    amount = po.amount,
                    subjectId = po.subjectId,
                    destAccountId = po.destAccountId,
                    sourceAccountId = po.sourceAccountId,
                    userId = po.userId
            )
        }
    }
}