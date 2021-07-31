package tech.igrant.jizhang.detail.it

class ItDetailQuery(
    val by: BY,
    val ids: List<Long>,
    val start: Long,
    val end: Long
)

enum class BY {
    SUBJECT
}