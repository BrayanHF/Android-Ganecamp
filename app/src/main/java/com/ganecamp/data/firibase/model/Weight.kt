package com.ganecamp.data.firibase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class Weight(
    @get:Exclude @set:Exclude var id: String? = null,
    val weight: Float = 0f,
    val date: Timestamp = Timestamp.now()
)