package tech.igrant.jizhang.detail

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tech.igrant.jizhang.ext.toJSON

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DetailServiceTest(
        @Autowired private val detailService: DetailService
) {

    @Test
    fun listBySubject() {
        val listBySubject = detailService.listBySubject(DetailQuery(
                subjectIds = arrayListOf(6, 7, 8, 9),
                page = 0,
                size = 15
        ))
        listBySubject.forEach { i -> println(i.toJSON()) }
    }
}