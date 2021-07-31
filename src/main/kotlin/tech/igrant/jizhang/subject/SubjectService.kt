package tech.igrant.jizhang.subject

import org.springframework.stereotype.Service

interface SubjectService {

    fun flat(ids: List<Long>): List<Subject>
    fun subjectMap(): Map<Long, Subject>

    @Service
    class Impl(
        private val subjectRepo: SubjectRepo
    ) : SubjectService {

        override fun flat(ids: List<Long>): List<Subject> {
            return subjectRepo.findChildrenByParents(ids)
        }

        fun findById(id: Long): Subject? {
            val opt = subjectRepo.findById(id)
            if (opt.isPresent) {
                return opt.get()
            }
            return null
        }

        override fun subjectMap(): Map<Long, Subject> {
            return subjectRepo.findAll().fold(
                mutableMapOf(),
                { acc, subject ->
                    subject.id?.let {
                        acc[it] = subject
                    }
                    acc
                }
            )
        }
    }

}