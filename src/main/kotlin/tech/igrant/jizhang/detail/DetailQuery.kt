package tech.igrant.jizhang.detail

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

@ApiModel("明细查询所传的对象")
data class DetailQuery(
        @ApiModelProperty("科目id列表")
        val subjectIds: List<Long>?,
        @ApiModelProperty("起始日期")
        val start: Date?,
        @ApiModelProperty("结束日期")
        val end: Date?,
        @ApiModelProperty("来源账户id")
        val sourceAccountId: Long?,
        @ApiModelProperty("目标账户id")
        val destAccountId: Long?
) {
    companion object {
        fun meaningful(detailQuery: DetailQuery): Boolean {
            return detailQuery.sourceAccountId != null ||
                    detailQuery.destAccountId != null ||
                    detailQuery.start != null ||
                    detailQuery.end != null ||
                    (!detailQuery.subjectIds.isNullOrEmpty())
        }
    }
}