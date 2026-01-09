package com.example.offers.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.design.Nav3SampleTheme

@Composable
fun OffersUi(
    onOfferClick: (id: Int) -> Unit,
    viewModel: OffersViewModel = hiltViewModel()
) {
    val offers by viewModel.offers.collectAsState()

    Nav3SampleTheme {
        LazyColumn {
            items(offers) { offer ->
                Text(
                    text = "Offer $offer", modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOfferClick(offer)
                        })
            }
        }
    }
}