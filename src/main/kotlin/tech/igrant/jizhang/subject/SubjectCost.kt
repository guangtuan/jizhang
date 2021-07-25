package tech.igrant.jizhang.subject

class SubjectCost(
    val unit: StatUnit,
    val display: String,
    val cost: Long,
    val subjectName: String
)

enum class StatUnit {
    Month
}