package tech.igrant.jizhang.event

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/event")
class EventController(private val eventService: EventService) {

    @GetMapping
    fun list(): ResponseEntity<List<EventVo>> {
        return ResponseEntity.of(Optional.ofNullable(eventService.list()))
    }

    @PostMapping
    fun create(@RequestBody eventTo: EventTo): ResponseEntity<Event> {
        return ResponseEntity.of(Optional.ofNullable(eventService.create(eventTo)))
    }

    @PostMapping("link")
    @ResponseBody
    fun linkAbsent(@RequestBody eventDetailLinkRequest: EventDetailLinkRequest): ResponseEntity<Any> {
        eventService.link(eventDetailLinkRequest)
        return ResponseEntity.ok("link success")
    }

}
