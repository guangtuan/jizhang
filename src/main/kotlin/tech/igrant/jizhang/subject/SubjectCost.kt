package tech.igrant.jizhang.subject

class SubjectStatQueryRequest(
    val ids: List<Long>,
    val start: Long,
    val end: Long,
    val level: Int
)

class SubjectCost(
    val unit: StatUnit,
    val display: String,
    val cost: Long,
    val subjectName: String
)

enum class StatUnit {
    Month
}