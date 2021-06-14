package tech.igrant.jizhang.event

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import tech.igrant.jizhang.detail.DetailRepo
import tech.igrant.jizhang.ext.FMT_YYYY_MM_dd_HH_mm_ss_SSS
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
interface EventService {

    fun list(): List<EventVo>

    fun create(eventTo: EventTo): Event

    fun link(eventDetailLinkRequest: EventDetailLinkRequest)

    fun eventMap(detailIds: List<Long>): Map<Long, Event>

    @Component
    class Impl(
        private val eventDetailRepo: EventDetailRepo,
        private val eventRepo: EventRepo,
        private val detailRepo: DetailRepo,
        private val jdbcTemplate: JdbcTemplate
    ) : EventService {

        override fun list(): List<EventVo> {
            val events = eventRepo.findAll()
            if (events.isEmpty()) {
                return emptyList()
            }
            val groupedByEventId = eventDetailRepo
                .findAllByEventIds(events.mapNotNull { it.id })
                .groupBy { it.eventId }
            val idToAmount = detailRepo.findAllById(groupedByEventId.values.flatten().map { it.detailId }).fold(
                mutableMapOf<Long, Int>(),
                { acc, curr ->
                    curr.id?.let {
                        acc[it] = curr.amount
                    }
                    acc
                }
            )
            return events.map { event ->
                EventVo(
                    name = event.name,
                    createdAt = event.createdAt,
                    sumAmount = groupedByEventId
                        .getOrDefault(event.id, emptyList())
                        .map { it.detailId }
                        .mapNotNull { idToAmount[it] }
                        .sum(),
                    countOfDetail = groupedByEventId.getOrDefault(event.id, emptyList()).size
                )
            }
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
            if (detailIds.isEmpty()) {
                return emptyMap()
            }
            val detailToEvent = eventDetailRepo.findAllByDetailIds(detailIds)
                .fold(
                    mutableMapOf<Long, Long>(),
                    { acc, eventDetail ->
                        acc[eventDetail.detailId] = eventDetail.eventId
                        acc
                    }
                )
            val lookup = eventRepo
                .findAllById(detailToEvent.values)
                .fold(
                    mutableMapOf<Long, Event>(),
                    { acc, e ->
                        e.id?.let {
                            acc[it] = e
                        }
                        acc
                    }
                )
            return detailIds
                .mapNotNull { detailId ->
                    detailToEvent[detailId]?.let { eventId ->
                        lookup[eventId]?.let { event ->
                            Pair(detailId, event)
                        }
                    }
                }
                .fold(
                    mutableMapOf(),
                    { acc, e ->
                        acc[e.first] = e.second
                        acc
                    }
                )
        }

    }

}
