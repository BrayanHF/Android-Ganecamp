package com.ganecamp.data.firibase.model

import com.google.firebase.Timestamp

data class WeightValue(
    val value: Float = 0f, val date: Timestamp = Timestamp.now()
)