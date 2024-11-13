package com.ganecamp.data.firibase.model

import com.google.firebase.firestore.Exclude

data class Event(
    @get:Exclude @set:Exclude var id: String? = null,
    val title: String = "",
    val description: String = ""
)