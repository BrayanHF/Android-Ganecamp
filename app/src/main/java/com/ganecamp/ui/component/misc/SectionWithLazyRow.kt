package com.ganecamp.ui.component.misc

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ganecamp.ui.component.button.ActionButton
import com.ganecamp.ui.theme.Green

@Composable
fun <T> SectionWithLazyRow(
    titleSection: String,
    itemsSection: List<T>,
    cardItem: @Composable (T) -> Unit,
    onClickAdd: () -> Unit,
    textButtonAdd: String? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titleSection,
                style = MaterialTheme.typography.titleSmall,
            )
            textButtonAdd?.let {
                ActionButton(text = textButtonAdd, color = Green, onClick = onClickAdd)
            }
        }
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
            items(itemsSection) { item ->
                cardItem(item)
            }
        }
    }
}