package com.example.offers.impl.details

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = OfferDetailsViewModel.Factory::class)
class OfferDetailsViewModel @AssistedInject constructor(
    @Assisted val id: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: String): OfferDetailsViewModel
    }
}
