package tech.igrant.jizhang.user

import org.springframework.stereotype.Service

@Service
class UserService(
        private val userRepo: UserRepo
) {

    fun findById(id: Long): User? {
        val opt = userRepo.findById(id)
        if (opt.isPresent) {
            return opt.get()
        }
        return null
    }

}