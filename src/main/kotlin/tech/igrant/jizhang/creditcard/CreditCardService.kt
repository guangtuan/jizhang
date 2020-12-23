package tech.igrant.jizhang.creditcard

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import tech.igrant.jizhang.user.UserService
import java.util.*

interface CreditCardRepo : CrudRepository<CreditCard, Long> {

}

interface CreditCardService {

    fun loadAll(): List<CreditCardVo>

    fun create(createRequest: CreditCardCreateRequest): Optional<CreditCardVo>

    fun update(id: Long, updateRequest: CreditCardUpdateRequest): Optional<CreditCardVo>

    @Service
    class Impl(
            private val creditCardRepo: CreditCardRepo,
            private val userService: UserService
    ) : CreditCardService {
        override fun loadAll(): List<CreditCardVo> {
            val creditCards = creditCardRepo.findAll()
            val userMap = userService.userMap(userIds = creditCards.map { it.userId }.distinct().toList())
            return creditCards.mapNotNull { c -> c.toVo(userMap[c.userId]?.nickname ?: "") }.toList()
        }

        override fun create(createRequest: CreditCardCreateRequest): Optional<CreditCardVo> {
            return userService.findById(createRequest.userId)?.let {
                val save = creditCardRepo.save(createRequest.toPo())
                if (save.id == null) {
                    return Optional.empty()
                }
                return Optional.of(save.toVo(it.nickname))
            } ?: Optional.empty()
        }

        override fun update(id: Long, updateRequest: CreditCardUpdateRequest): Optional<CreditCardVo> {
            return userService.findById(updateRequest.userId)?.let { user ->
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
                                    name = updateRequest.name,
                                    userId = user.id!!
                            )
                    ).toVo(user.nickname)
                }
            } ?: Optional.empty()
        }

    }

}