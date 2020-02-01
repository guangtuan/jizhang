package tech.igrant.jizhang.detail

import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import tech.igrant.jizhang.framework.PageQuery

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class DetailServiceTest(
        @Autowired private val detailService: DetailService
) {

    @Test
    fun listBySubject() {
        var query = PageQuery(
                queryParam = DetailQuery(subjectIds = arrayListOf(6, 7, 8, 9)),
                page = 0,
                size = 15
        )
        var result = detailService.listBySubject(query)
        val standardTotal = result.total
        while (result.hasNext()) {
            query = query.next()
            result = detailService.listBySubject(query)
            Assert.assertEquals(
                    Math.min(query.size, result.content.size),
                    result.content.size
            )
            Assert.assertEquals(
                    standardTotal,
                    result.total
            )
        }
    }

}