package com.example.trainymvp.ui.item

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.trainymvp.R
import com.example.trainymvp.data.Item
import com.example.trainymvp.data.TimePreset

@Composable
fun WPItem(
    item: Item,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        onClick = { expanded = !expanded }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(id = R.string.person_content_desc),
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(dimensionResource(id = R.dimen.padding_small))
                )
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                WPItemButton(
                    expanded = expanded
                )
            }
            if (expanded) {
                ExpandedInformation(
                    description = item.description
                )
            }
        }
    }
}

@Composable
fun WPItemButton(
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription = stringResource(R.string.expand_button_content_description),
        tint = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}

@Composable
fun ExpandedInformation(
    description: String,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            maxLines = 2,
            modifier = Modifier.widthIn(0.dp, 160.dp)
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { /*TODO*/ }
        ) {
            Text(
                text = "Start",
                style = MaterialTheme.typography.labelLarge
            )
        }
        Spacer(Modifier.padding(dimensionResource(id = R.dimen.padding_very_small)))
        FilledTonalIconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Rounded.Edit,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(id = R.string.person_content_desc)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryItemPreview() {
    AppTheme {
        Column(
            Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            WPItem(
                Item(
                    0,
                    "Title",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit."
                )
            )
            WPItem(
                Item(
                    1,
                    "Title",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit."
                )
            )
            WPItem(
                Item(
                    2,
                    "Title",
                    "Lorem ipsum dolor sit amet consectetur adipiscing elit."
                )
            )
        }
    }
}