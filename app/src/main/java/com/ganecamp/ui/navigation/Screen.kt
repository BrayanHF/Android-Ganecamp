package com.ganecamp.ui.navigation

import com.ganecamp.R

sealed class Screen(val route: String, val title: Int, val icon: Int) {
    data object Animal : Screen("animal", R.string.animals, R.drawable.ic_animals)
    data object Lot : Screen("lot", R.string.lots, R.drawable.ic_lot)
    data object Scan : Screen("scan", R.string.scan, R.drawable.ic_scan)
}

val bottomBarScreens = setOf(
    Screen.Animal.route,
    Screen.Lot.route,
    Screen.Scan.route
)