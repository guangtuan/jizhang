package tech.igrant.jizhang.creditcard

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class CreditCard(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long?,
        var createdAt: Date,
        var dateBill: Int,
        var dateRepay: Int,
        var amountLimit: Int,
        var updatedAt: Date?
) {
    fun toVo(): CreditCardVo {
        return CreditCardVo(
                id = id!!,
                dateBill = this.dateBill,
                dateRepay = this.dateRepay,
                amountLimit = this.amountLimit,
                createdAt = this.createdAt,
                updatedAt = this.updatedAt!!
        )
    }
}

data class CreditCardCreateRequest(
        private val dateBill: Int,
        private val dateRepay: Int,
        private val amountLimit: Int
) {
    fun toPo(): CreditCard {
        return CreditCard(
                id = null,
                createdAt = Date(),
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
        val amountLimit: Int
)

data class CreditCardVo(
        val id: Long,
        private val dateBill: Int,
        private val dateRepay: Int,
        private val amountLimit: Int,
        private val createdAt: Date,
        private val updatedAt: Date?
)