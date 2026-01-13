package com.example.offers.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.design.Nav3SampleTheme

@Composable
fun OffersUi(
    onOfferClick: (id: Int) -> Unit,
    viewModel: OffersViewModel = hiltViewModel()
) {
    val offers by viewModel.offers.collectAsState()

    OffersContent(
        offers = offers,
        onOfferClick = onOfferClick
    )
}

@Composable
private fun OffersContent(
    offers: List<Int>,
    onOfferClick: (id: Int) -> Unit
) {
    LazyColumn {
        items(offers) { offer ->
            OfferItem(
                offerId = offer,
                onClick = { onOfferClick(offer) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
        }
    }
}

@Composable
private fun OfferItem(
    offerId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Offer $offerId",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "View offer details",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OffersContentPreview() {
    Nav3SampleTheme {
        OffersContent(
            offers = listOf(1, 2, 3, 4, 5),
            onOfferClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OfferItemPreview() {
    Nav3SampleTheme {
        OfferItem(
            offerId = 42,
            onClick = {}
        )
    }
}