package com.ganecamp.model.objects

import com.google.firebase.Timestamp

data class WeightValue(
    val value: Float = 0f, val date: Timestamp = Timestamp.now()
)