package com.ganecamp.ui.util

import java.math.BigDecimal
import java.math.RoundingMode

fun sanitizeNumberDuringTyping(input: String): String {
    val sanitizedInput = input.filter { it.isDigit() || it == '.' || it == ',' }
    return sanitizedInput.replace(",", ".")
}

fun formatNumber(input: String): String {
    return try {
        val number = BigDecimal(input).setScale(2, RoundingMode.HALF_UP)
        if (number == BigDecimal.ZERO) {
            ""
        } else {
            number.stripTrailingZeros().toPlainString()
        }
    } catch (e: Exception) {
        ""
    }
}