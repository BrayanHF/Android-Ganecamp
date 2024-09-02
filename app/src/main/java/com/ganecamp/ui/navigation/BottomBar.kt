package com.ganecamp.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ganecamp.R
import com.ganecamp.ui.theme.Black
import com.ganecamp.ui.theme.DarkGreen
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography

@Composable
fun BottomBar(navController: NavHostController) {
    NavigationBar(containerColor = LightGreen, contentColor = Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        bottomBarItems.forEach { item ->
            NavigationBarItem(label = {
                Text(
                    text = stringResource(id = item.titleRes), style = Typography.titleSmall
                )
            }, icon = {
                Icon(
                    painter = painterResource(id = item.iconRes),
                    contentDescription = stringResource(id = item.titleRes),
                    modifier = Modifier.height(32.dp)
                )
            }, selected = currentDestination == item.screen, onClick = {
                navController.navigate(item.screen) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, colors = NavigationBarItemDefaults.colors(indicatorColor = DarkGreen)
            )
        }
    }
}

data class BottomBarItem(
    val screen: String, val titleRes: Int, val iconRes: Int
)

val bottomBarItems = listOf(
    BottomBarItem("com.ganecamp.ui.navigation.AnimalsNav", R.string.animals, R.drawable.ic_animals),
    BottomBarItem("com.ganecamp.ui.navigation.LotsNav", R.string.lots, R.drawable.ic_lot),
    BottomBarItem("com.ganecamp.ui.navigation.ScanNav", R.string.scan, R.drawable.ic_scan)
)