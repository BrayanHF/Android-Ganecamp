package com.ganecamp.data.firibase.model

import com.google.firebase.firestore.Exclude

data class Farm(
    @get:Exclude @set:Exclude var id: String? = null,
    val token: String = "",
    val location: String = "",
    val name: String = "",
    val owner: String = ""
)