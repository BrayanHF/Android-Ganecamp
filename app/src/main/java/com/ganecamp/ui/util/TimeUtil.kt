package com.ganecamp.ui.util

import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtil {
    val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("UTC"))
}