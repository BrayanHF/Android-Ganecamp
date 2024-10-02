package com.ganecamp.model.objects

import com.google.firebase.firestore.Exclude

data class Vaccine(
    @get:Exclude @set:Exclude var id: String? = null,
    val name: String = "",
    val description: String = ""
)