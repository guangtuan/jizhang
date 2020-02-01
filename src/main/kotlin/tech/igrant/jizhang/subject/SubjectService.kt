package tech.igrant.jizhang.subject

import org.springframework.stereotype.Service

@Service
class SubjectService(
        private val subjectRepo: SubjectRepo
) {

    fun findById(id: Long): Subject? {
        val opt = subjectRepo.findById(id)
        if (opt.isPresent) {
            return opt.get()
        }
        return null
    }

}