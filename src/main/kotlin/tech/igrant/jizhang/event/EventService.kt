package tech.igrant.jizhang.event

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import java.sql.Date

@Component
interface EventService {

    fun list(): List<Event>

    fun create(eventTo: EventTo): Event

    fun link(eventDetailLinkRequest: EventDetailLinkRequest)

    @Component
    class Impl(private val eventRepo: EventRepo, private val jdbcTemplate: JdbcTemplate) : EventService {

        override fun list(): List<Event> {
            return eventRepo.findAll().filterNotNull()
        }

        override fun create(eventTo: EventTo): Event {
            return eventRepo.save(eventTo.toPo())
        }

        override fun link(eventDetailLinkRequest: EventDetailLinkRequest) {
            val clearSql = "delete from event_detail where detail_id = ?"
            jdbcTemplate.update(clearSql) {
                it.setLong(1, eventDetailLinkRequest.detailId)
            }
            val sql = "insert into event_detail (event_id, detail_id, created_at) values(?,?,?)";
            jdbcTemplate.update(sql) {
                it.setLong(1, eventDetailLinkRequest.eventId)
                it.setLong(2, eventDetailLinkRequest.detailId)
                it.setDate(3, Date(java.util.Date().time))
            }
        }

    }

}
