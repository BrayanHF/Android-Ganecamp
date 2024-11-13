package com.ganecamp.domain.model.interfaces

import com.google.firebase.Timestamp

interface Applied {
    var id: String?
    val date: Timestamp
    val description: String
}