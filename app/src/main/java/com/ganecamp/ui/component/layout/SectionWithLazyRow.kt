package com.ganecamp.ui.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.ui.component.button.ActionButton
import com.ganecamp.ui.component.button.HideButton
import com.ganecamp.ui.theme.Green

@Composable
fun <T> SectionWithLazyRow(
    titleSection: String,
    itemsSection: List<T>,
    cardItem: @Composable (item: T) -> Unit,
    onClickAdd: () -> Unit,
    textButtonAdd: String? = null
) {
    Column {
        val showContent = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HideButton(text = titleSection,
                showContent = showContent.value,
                onClick = { showContent.value = !showContent.value })
            textButtonAdd?.let {
                ActionButton(text = textButtonAdd, color = Green, onClick = onClickAdd)
            }
        }
        if (showContent.value) {
            if (itemsSection.isNotEmpty()) {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(itemsSection) { itemSection ->
                        cardItem(itemSection)
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.no_items),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}