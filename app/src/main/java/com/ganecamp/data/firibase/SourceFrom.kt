package com.ganecamp.data.firibase

import com.ganecamp.domain.network.NetworkStatusHelper
import com.google.firebase.firestore.Source

fun NetworkStatusHelper.getSourceFrom(): Source {
    return if (this.isConnectedNetwork.value) {
        Source.DEFAULT
    } else {
        Source.CACHE
    }
}