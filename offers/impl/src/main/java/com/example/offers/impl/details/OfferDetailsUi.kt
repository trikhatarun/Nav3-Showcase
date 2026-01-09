package com.example.offers.impl.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.design.Nav3SampleTheme

@Composable
fun OfferDetailsUi(
    id: Int,
    viewModel: OfferDetailsViewModel = hiltViewModel<OfferDetailsViewModel, OfferDetailsViewModel.Factory>(
        creationCallback = { it.create(id) }
    )
) {
    Nav3SampleTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Offer Details - ID: ${viewModel.id}")
        }
    }
}

