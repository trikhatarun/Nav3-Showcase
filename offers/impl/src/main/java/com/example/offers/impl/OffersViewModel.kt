package com.example.offers.impl

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OffersViewModel @Inject constructor() : ViewModel() {

    private val _offers = MutableStateFlow(listOf(1, 2, 3, 4, 5))
    val offers: StateFlow<List<Int>> = _offers
}

