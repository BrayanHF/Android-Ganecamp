package com.ganecamp.ui.navigation

import com.ganecamp.R

sealed class Screen(val route: String, val title: Int, val icon: Int) {
    object Animal : Screen("animal", R.string.animals, R.drawable.ic_prub_anim)
    object Lot : Screen("lot", R.string.lots, R.drawable.ic_prub_anim)
    object Scan : Screen("scan", R.string.scan, R.drawable.ic_prub_anim)
}
