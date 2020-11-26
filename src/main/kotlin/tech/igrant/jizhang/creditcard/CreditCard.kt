package tech.igrant.jizhang.creditcard

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class CreditCard(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
        var name: String,
        var createdAt: Date,
        var dateBill: Int,
        var dateRepay: Int,
        var amountLimit: Int,
        var updatedAt: Date?
) {
    fun toVo(): CreditCardVo {
        return CreditCardVo(
                id = id!!,
                name = this.name,
                dateBill = this.dateBill,
                dateRepay = this.dateRepay,
                amountLimit = this.amountLimit,
                createdAt = this.createdAt,
                updatedAt = this.updatedAt
        )
    }
}

data class CreditCardCreateRequest(
        val dateBill: Int,
        val dateRepay: Int,
        val amountLimit: Int,
        var name: String
) {
    fun toPo(): CreditCard {
        return CreditCard(
                id = null,
                createdAt = Date(),
                name = this.name,
                dateBill = this.dateBill,
                dateRepay = this.dateRepay,
                amountLimit = this.amountLimit,
                updatedAt = null
        )
    }
}

data class CreditCardUpdateRequest(
        val dateBill: Int,
        val dateRepay: Int,
        val amountLimit: Int,
        val name: String
)

data class CreditCardVo(
        val id: Long,
        var name: String,
        val dateBill: Int,
        val dateRepay: Int,
        val amountLimit: Int,
        val createdAt: Date,
        val updatedAt: Date?
)