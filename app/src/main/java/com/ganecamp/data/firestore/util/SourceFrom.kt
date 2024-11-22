package com.ganecamp.data.firestore.util

import com.ganecamp.data.network.NetworkStatusHelper
import com.google.firebase.firestore.Source

fun NetworkStatusHelper.getSourceFrom(): Source {
    return if (this.isConnectedNetwork.value) {
        Source.DEFAULT
    } else {
        Source.CACHE
    }
}