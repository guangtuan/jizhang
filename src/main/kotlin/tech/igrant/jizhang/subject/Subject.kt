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
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)