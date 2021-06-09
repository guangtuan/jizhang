package tech.igrant.jizhang.event

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import tech.igrant.jizhang.ext.FMT_YYYY_MM_dd_HH_mm_ss_SSS
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
interface EventService {

    fun list(): List<Event>

    fun create(eventTo: EventTo): Event

    fun link(eventDetailLinkRequest: EventDetailLinkRequest)

    fun eventMap(detailIds: List<Long>): Map<Long, Event>

    @Component
    class Impl(
        private val eventDetailRepo: EventDetailRepo,
        private val eventRepo: EventRepo,
        private val jdbcTemplate: JdbcTemplate
    ) : EventService {

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
                it.setString(3, LocalDateTime.now().format(DateTimeFormatter.ofPattern(FMT_YYYY_MM_dd_HH_mm_ss_SSS)))
            }
        }

        override fun eventMap(detailIds: List<Long>): Map<Long, Event> {
            val ret = eventDetailRepo.findAllByDetailIds(detailIds)
                .fold(
                    mutableMapOf<Long, Long>(),
                    { acc, eventDetail ->
                        acc[eventDetail.eventId] = eventDetail.detailId
                        acc
                    }
                )
            return eventRepo
                .findAllById(ret.keys)
                .fold(
                    mutableMapOf(),
                    { acc, e ->
                        e.id?.let { eventId ->
                            ret[eventId]?.let { detailId ->
                                acc[detailId] = e
                            }
                        }
                        acc
                    }
                )
        }

    }

}
