package tech.igrant.jizhang.subject

import tech.igrant.jizhang.ext.toLocalDateTime
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Subject(
        var name: String,
        var description: String,
        val parentId: Long? = null,
        val level: Int,
        val createdAt: Date,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
) {
    companion object {
        const val LEVEL_BIG = 1
        const val LEVEL_SMALL = 2
    }

    fun toVo(parent: String?): SubjectVo {
        return SubjectVo(
                name = this.name,
                description = this.description,
                id = this.id!!,
                level = this.level,
                children = mutableListOf(),
                parentId = this.parentId,
                parent = parent,
                createdAt = this.createdAt.toLocalDateTime()
        )
    }
}

class SubjectVo(
        val name: String,
        val description: String,
        val id: Long,
        val children: MutableList<SubjectVo>,
        val parentId: Long?,
        val parent: String?,
        val level: Int,
        val createdAt: LocalDateTime
)

class SubjectTo(
        private val name: String,
        private val description: String,
        private val parentId: Long?,
        private val level: Int
) {
    fun toPo(po: Subject?): Subject {
        return po?.let {
            Subject(
                    id = po.id,
                    level = po.level,
                    name = this.name,
                    parentId = po.parentId,
                    createdAt = po.createdAt,
                    description = this.description
            )
        } ?: Subject(
                id = null,
                name = this.name,
                level = this.level,
                createdAt = Date(),
                parentId = this.parentId,
                description = this.description
        )
    }
}