package com.ganecamp.domain.model

data class GanecampUser(
    val id: String? = null,
    var email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val farmToken: String = ""
)