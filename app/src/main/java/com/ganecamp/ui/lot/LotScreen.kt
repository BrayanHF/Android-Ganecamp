package com.ganecamp.ui.lot

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.R
import com.ganecamp.domain.model.Lot
import com.ganecamp.ui.general.GeneralSurface
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.general.NoRegistered
import com.ganecamp.ui.theme.DarkGreen
import com.ganecamp.ui.theme.Typography
import com.ganecamp.ui.theme.White

@Composable
fun LotScreen(navController: NavController) {
    val viewModel: LotViewModel = hiltViewModel()
    val lots by viewModel.lots.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = true)

    LaunchedEffect(key1 = navController.currentBackStackEntry) {
        viewModel.loadLots()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(White)
    ) {
        if (isLoading) {
            IsLoading()
        } else {
            LotList(navController, lots)
        }

        FloatingActionButton(
            onClick = { navController.navigate("formLot/0") },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
                .size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            containerColor = Color.Transparent
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(id = R.string.add),
                tint = DarkGreen,
                modifier = Modifier.background(Color.Transparent)
            )
        }
    }
}

@Composable
fun LotList(navController: NavController, lots: List<Lot>) {
    if (lots.isEmpty()) {
        NoRegistered(textId = R.string.no_lots)
        return
    }

    val columns: Int =
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            2
        }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        items(lots.size) {
            LotItem(navController, lots[it])
        }
    }
}

@Composable
fun LotItem(navController: NavController, lot: Lot) {
    GeneralSurface(onClick = {
        navController.navigate("lotDetail/${lot.id}")
    }) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (id, animalCount, bottomLine) = createRefs()

            Column(
                modifier = Modifier.constrainAs(id) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(44.dp)
                }, verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ID: ${lot.id}",
                    style = Typography.titleSmall,
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 0.dp)
                )
            }

            Column(
                modifier = Modifier.constrainAs(animalCount) {
                    top.linkTo(id.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(44.dp)
                }, verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.animals) + ": ${lot.animalCount}",
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 4.dp)
                )
            }

            Box(modifier = Modifier
                .constrainAs(bottomLine) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(4.dp)
                }
                .background(DarkGreen))
        }
    }
}
