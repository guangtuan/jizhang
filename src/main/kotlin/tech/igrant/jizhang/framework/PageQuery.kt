package tech.igrant.jizhang.framework

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("分页查询参数封装")
class PageQuery<T>(
        val queryParam: T,
        @ApiModelProperty("分页查询当前页码，从0开始")
        val page: Int,
        @ApiModelProperty("分页查询页大小")
        val size: Int
) {
    fun next(): PageQuery<T> {
        return PageQuery(
                queryParam = queryParam,
                page = page + 1,
                size = size
        )
    }
}