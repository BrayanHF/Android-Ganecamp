package com.ganecamp.domain.model

import com.ganecamp.data.database.entities.LotEntity
import java.time.ZonedDateTime

data class LotDetail(
    val id: Int,
    val purchaseValue: Double,
    val purchaseDate: ZonedDateTime,
    val saleValue: Double,
    val saleDate: ZonedDateTime
)

fun LotEntity.toDomain() = LotDetail(id, purchaseValue, purchaseDate, saleValue, saleDate)