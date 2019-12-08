package tech.igrant.jizhang

import org.apache.commons.lang3.time.DateUtils
import org.assertj.core.util.DateUtil
import org.junit.jupiter.api.Test
import tech.igrant.jizhang.ext.getStartOfTomorrow
import java.util.*

class AnyTest {

    @Test
    fun testDateUtil() {
        val d = Date()
        println(DateUtil.formatAsDatetime(d))
        val tomorrow = DateUtils.addDays(d, 1)
        println(DateUtil.formatAsDatetime(tomorrow))
        val startOfTomorrow = DateUtils.setSeconds(DateUtils.setMinutes(DateUtils.setHours(tomorrow, 0), 0), 0)
        println(DateUtil.formatAsDatetime(startOfTomorrow))
    }

    @Test
    fun testExt() {
        val d = Date()
        println(DateUtil.formatAsDatetime(d))
        val startOfTomorrow = d.getStartOfTomorrow()
        println(DateUtil.formatAsDatetime(startOfTomorrow))
    }

}