package com.ganecamp.domain.usecase.animal

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class CalculateAnimalAgeUseCase @Inject constructor() {
    operator fun invoke(birthInstant: Instant): Triple<Int, Int, Int> {
        val birthDate = birthInstant.atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()

        val years = ChronoUnit.YEARS.between(birthDate, today).toInt()

        val months = ChronoUnit.MONTHS.between(birthDate.plusYears(years.toLong()), today).toInt()

        val days = ChronoUnit.DAYS.between(birthDate.plusYears(years.toLong()).plusMonths(months.toLong()), today).toInt()

        return Triple(years, months, days)
    }
}