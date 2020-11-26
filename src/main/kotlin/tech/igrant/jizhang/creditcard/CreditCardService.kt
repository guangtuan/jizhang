package tech.igrant.jizhang.creditcard

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.util.*

interface CreditCardRepo : CrudRepository<CreditCard, Long> {

}

interface CreditCardService {

    fun loadAll(): List<CreditCardVo>

    fun create(createRequest: CreditCardCreateRequest): Optional<CreditCardVo>

    fun update(id: Long, updateRequest: CreditCardUpdateRequest): Optional<CreditCardVo>

    @Service
    class Impl(
            private val creditCardRepo: CreditCardRepo
    ) : CreditCardService {
        override fun loadAll(): List<CreditCardVo> {
            return creditCardRepo.findAll().mapNotNull { c -> c.toVo() }.toList()
        }

        override fun create(createRequest: CreditCardCreateRequest): Optional<CreditCardVo> {
            val save = creditCardRepo.save(createRequest.toPo())
            if (save.id == null) {
                return Optional.empty()
            }
            return Optional.of(save.toVo())
        }

        override fun update(id: Long, updateRequest: CreditCardUpdateRequest): Optional<CreditCardVo> {
            val dataInDb = creditCardRepo.findById(id)
            return dataInDb.map {
                creditCardRepo.save(
                        CreditCard(
                                id = id,
                                createdAt = it.createdAt,
                                updatedAt = Date(),
                                dateBill = updateRequest.dateBill,
                                dateRepay = updateRequest.dateRepay,
                                amountLimit = updateRequest.amountLimit,
                                name = updateRequest.name
                        )
                ).toVo()
            }
        }

    }

}