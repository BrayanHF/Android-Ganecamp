package com.ganecamp.ui.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ganecamp.R
import com.ganecamp.data.firibase.model.Animal
import com.ganecamp.domain.enums.AnimalGender
import com.ganecamp.ui.util.getAnimalBreedRes
import com.ganecamp.ui.util.getAnimalStateRes

@Composable
fun AnimalCard(animal: Animal, showLot: Boolean = false, onClick: () -> Unit) {
    val animalGenderIcon: Int = if (animal.animalGender == AnimalGender.Male) {
        R.drawable.ic_bull
    } else {
        R.drawable.ic_cow
    }
    val (textRes, colorRes) = getAnimalStateRes(animal.animalState)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                animal.nickname?.let { nickname ->
                    Text(
                        text = nickname,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                    )
                }
                Text(
                    text = stringResource(id = R.string.tag) + ": ${animal.tag}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 0.dp)
                )
            }
            if (animal.lotId != null && showLot) {
                Text(
                    text = stringResource(id = R.string.lot) + ": ${animal.lotId}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = animalGenderIcon),
                contentDescription = stringResource(R.string.animal_icon),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = getAnimalBreedRes(animal.animalBreed)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = colorRes.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = textRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorRes,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}