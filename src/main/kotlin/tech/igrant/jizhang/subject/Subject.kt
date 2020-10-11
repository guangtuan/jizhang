package tech.igrant.jizhang.subject

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Subject (
        var name: String,
        var description: String,
        var tags: String,
        val parentId: Long? = null,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
) {
    fun toVo(): SubjectVo {
        return SubjectVo(
                name = this.name,
                description = this.description,
                id = this.id!!,
                children = mutableListOf(),
                parentId = this.parentId
        )
    }
}

class SubjectVo (
        val name: String,
        val description: String,
        val id: Long,
        val children: MutableList<SubjectVo>,
        val parentId: Long?
)

class SubjectTo (
        private val name: String,
        private val tags: String,
        private val description: String,
        private val parentId: Long
) {
    fun toPo(): Subject {
        return Subject(
                name = name,
                description = description,
                parentId = parentId,
                tags = tags,
                id = null
        )
    }
}