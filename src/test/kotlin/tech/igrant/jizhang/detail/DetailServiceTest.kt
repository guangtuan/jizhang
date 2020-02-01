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
        var firstResult = detailService.listBySubject(query)
        val standardTotal = firstResult.total
        while (firstResult.hasNext()) {
            query = query.next()
            firstResult = detailService.listBySubject(query)
            Assert.assertEquals(
                    standardTotal,
                    firstResult.total
            )
        }
    }

}