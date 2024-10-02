package com.ganecamp.model.objects

import com.ganecamp.model.interfaces.Applied
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class VaccineApplied(
    @get:Exclude @set:Exclude override var id: String? = null,
    val vaccineId: String = "",
    override val date: Timestamp = Timestamp.now(),
    val name: String = "",
    override val description: String = ""
) : Applied