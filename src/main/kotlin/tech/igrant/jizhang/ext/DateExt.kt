package tech.igrant.jizhang.ext

import org.apache.commons.lang3.time.DateUtils
import java.util.*

fun Date.getStartOfTomorrow(): Date {
    return DateUtils.setSeconds(
            DateUtils.setMinutes(
                    DateUtils.setHours(
                            DateUtils.addDays(this, 1), 0
                    ), 0
            ), 0
    )
}