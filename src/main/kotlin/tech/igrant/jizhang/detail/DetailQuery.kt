package tech.igrant.jizhang.detail

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("明细查询所传的对象")
data class DetailQuery(
        @ApiModelProperty("科目id列表")
        val subjectIds: List<Long>,
        @ApiModelProperty("页码，从0开始")
        val page: Int,
        @ApiModelProperty("页的大小")
        val size: Int
)