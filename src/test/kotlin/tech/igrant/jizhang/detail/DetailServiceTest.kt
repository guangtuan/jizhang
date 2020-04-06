package tech.igrant.jizhang.detail

import org.apache.commons.lang3.time.DateUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import tech.igrant.jizhang.JizhangApplication
import tech.igrant.jizhang.framework.PageQuery

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = arrayOf(JizhangApplication::class))
class DetailServiceTest {

    @Autowired
    lateinit var detailService: DetailService

    @Test
    fun listBySubject() {
        var query = PageQuery(
                queryParam = DetailQuery(
                        subjectIds = arrayListOf(6),
                        start = DateUtils.parseDate("2019-09-05 00:00:00", "yyyy-MM-dd HH:mm:ss"),
                        end = DateUtils.parseDate("2019-09-06 00:00:00", "yyyy-MM-dd HH:mm:ss"),
                        destAccountId = null,
                        sourceAccountId = null
                ),
                page = 0,
                size = 50
        )
        var result = detailService.query(query)
        val standardTotal = result.total
        while (result.hasNext()) {
            query = query.next()
            result = detailService.query(query)
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