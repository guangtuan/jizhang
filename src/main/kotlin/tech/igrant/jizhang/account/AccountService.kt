package tech.igrant.jizhang.account

import org.springframework.stereotype.Service

@Service
class AccountService(
        private val accountRepo: AccountRepo
) {

    fun findById(id: Long): Account? {
        val opt = accountRepo.findById(id)
        if (opt.isPresent) {
            return opt.get()
        }
        return null
    }

}