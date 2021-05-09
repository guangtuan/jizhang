package tech.igrant.jizhang.detail

import java.util.*

class AmountTotal(
        val subjectId: Long,
        val subjectName: String,
        val total: Long
)

class AmountTotalQuery(
        val start: Date,
        val end: Date,
        val level: Int,
        val subjects: List<Long>
)