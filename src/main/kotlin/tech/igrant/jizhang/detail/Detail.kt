package tech.igrant.jizhang.detail

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Detail(
        var userId: Long,
        var sourceAccountId: Long? = null,
        var destAccountId: Long? = null,
        var subjectId: Long,
        var remark: String?,
        var createdAt: Date?,
        var updatedAt: Date?,
        var amount: Int,
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

data class DetailVo(
        var id: Long,
        var username: String?,
        var sourceAccountName: String?,
        var destAccountName: String?,
        var subjectName: String?,
        var remark: String?,
        var createdAt: Date?,
        var updatedAt: Date?,
        var amount: Int
) {
    companion object {
        fun fromPo(
                po: Detail,
                username: String,
                sourceAccountName: String,
                destAccountName: String,
                subjectName: String
        ): DetailVo {
            return DetailVo(
                    id = po.id!!,
                    username = username,
                    sourceAccountName = sourceAccountName,
                    destAccountName = destAccountName,
                    subjectName = subjectName,
                    remark = po.remark,
                    createdAt = po.createdAt,
                    updatedAt = po.updatedAt,
                    amount = po.amount
            )
        }
    }
}