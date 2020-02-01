package tech.igrant.jizhang.framework

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("分页查询结果")
class PageResult<T>(
        @ApiModelProperty("分页查询结果内容")
        val content: List<T>,
        @ApiModelProperty("分页查询当前页码")
        val page: Int,
        @ApiModelProperty("分页查询页大小")
        val size: Int,
        @ApiModelProperty("分页查询总数")
        val total: Long
) {
    fun hasNext(): Boolean {
        return (this.page + 1) * this.size < this.total
    }
}