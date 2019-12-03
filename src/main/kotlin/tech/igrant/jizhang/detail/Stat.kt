package tech.igrant.jizhang.detail

import tech.igrant.jizhang.subject.Subject
import java.util.*

class Stat(
        val start: Date,
        val end: Date,
        val subjects: List<Subject>,
        val amounts: Map<Subject, Int>
)

class StatDetail(
        val subjectId: Long,
        val subjectName: String,
        val total: Long
)