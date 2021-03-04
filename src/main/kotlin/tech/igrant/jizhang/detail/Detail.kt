package tech.igrant.jizhang.detail

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import tech.igrant.jizhang.ext.toLocalDateTime
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Detail(
        var userId: Long,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        var remark: String?,
        var createdAt: LocalDateTime,
        var updatedAt: LocalDateTime?,
        var amount: Int,
        var splited: Int,
        var parentId: Int?,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
) {
    companion object {
        const val NOT_SPLITED = 0
        const val SPLITED = 1
        const val SPLIT_PARENT = 2
    }
}

data class DetailTo (
        var userId: Long,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        var remark: String?,
        var createdAt: Date,
        var updatedAt: Date?,
        var amount: Int,
        var splited: Int,
        var parentId: Int?
) {
    fun toDomain(): Detail {
        return Detail(
                userId = this.userId,
                sourceAccountId = this.sourceAccountId,
                destAccountId =  this.destAccountId,
                subjectId = this.subjectId,
                remark = this.remark,
                createdAt = this.createdAt.toLocalDateTime(),
                updatedAt = this.updatedAt?.toLocalDateTime(),
                amount = this.amount,
                splited = this.splited,
                parentId = this.parentId,
                id = null
        )
    }
}

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
        val amount: Int? = null,
        @ApiModelProperty("明细发生时间")
        val createdAt: Date? = null,
        @ApiModelProperty("是否为分摊出来的明细")
        var splited: Int = Detail.NOT_SPLITED,
        @ApiModelProperty("如果是分摊出来的明细，这个字段表示分摊的来源")
        var parentId: Int?
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
        var createdAt: LocalDateTime?,
        var updatedAt: LocalDateTime?,
        var amount: Int,
        @ApiModelProperty("是否由分摊出来的明细")
        var splited: Int?,
        @ApiModelProperty("如果是分摊出来的明细，这个字段表示分摊的来源")
        var parentId: Int?
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
                    userId = po.userId,
                    splited = po.splited,
                    parentId = po.parentId
            )
        }
    }
}