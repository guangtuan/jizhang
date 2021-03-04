package tech.igrant.jizhang.ext

import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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


fun Date.fmt(pattern: String): String {
    return DateFormatUtils.format(this, pattern)
}

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun LocalDateTime.toDate(): Date {
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

fun Date.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());
}

const val FMT_YYYY_MM_dd_HH_mm_ss = "YYYY-MM-dd HH:mm:ss"