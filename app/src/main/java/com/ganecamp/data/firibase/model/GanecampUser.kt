package com.ganecamp.data.firibase.model

import com.google.firebase.firestore.Exclude

data class GanecampUser(
    @get:Exclude @set:Exclude var id: String? = null,
    var email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val farmToken: String = ""
)