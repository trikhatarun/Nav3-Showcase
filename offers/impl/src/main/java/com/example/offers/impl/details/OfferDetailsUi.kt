package com.example.offers.impl.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.design.Nav3SampleTheme

@Composable
fun OfferDetailsUi(
    id: Int,
    onProfileClick: () -> Unit,
    viewModel: OfferDetailsViewModel = hiltViewModel<OfferDetailsViewModel, OfferDetailsViewModel.Factory>(
        creationCallback = { it.create(id) }
    )
) {
    Nav3SampleTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Offer Details - ID: ${viewModel.id}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onProfileClick) {
                Text(text = "Profile")
            }
        }
    }
}

