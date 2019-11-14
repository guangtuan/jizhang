package tech.igrant.jizhang.detail

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Detail(
        var user: String ?,
        var source: String ?,
        var dest: String ?,
        var subject: String,
        var remark: String ?,
        var createdAt: Date ?,
        var updatedAt: Date ?,
        var amount: Int ?,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)