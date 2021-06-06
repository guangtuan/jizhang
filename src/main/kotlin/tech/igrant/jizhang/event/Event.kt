package tech.igrant.jizhang.event

import org.springframework.data.jpa.repository.JpaRepository
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

class EventDetailLinkRequest(val detailId: Long, val eventId: Long)

interface EventRepo : JpaRepository<Event, Long>