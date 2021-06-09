package tech.igrant.jizhang.event

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Event(
    var name: String,
    var createdAt: LocalDateTime,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

data class EventTo(val name: String) {
    fun toPo(): Event {
        return Event(
            name = this.name,
            createdAt = LocalDateTime.now()
        )
    }
}

@Entity
data class EventDetail(
    val detailId: Long,
    val eventId: Long,
    val createdAt: LocalDateTime,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null
)

class EventDetailLinkRequest(val detailId: Long, val eventId: Long)

interface EventRepo : JpaRepository<Event, Long>

interface EventDetailRepo : JpaRepository<EventDetail, Long> {

    @Query(nativeQuery = true, value = "select * from event_detail where detail_id in :detailIds")
    fun findAllByDetailIds(@Param("detailIds") detailIds: List<Long>): List<EventDetail>

}