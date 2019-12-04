package tech.igrant.jizhang.detail

import java.util.*

class StatDetail(
        val subjectId: Long,
        val subjectName: String,
        val total: Long
)

class StatQuery(
        val start: Date,
        val end: Date,
        val subjects: List<Long>
)