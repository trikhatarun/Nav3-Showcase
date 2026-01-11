package com.example.offers.api

import androidx.navigation3.runtime.NavKey

object OffersNavigation {
    object Offers : NavKey
    data class OfferDetails(val id: Int) : NavKey
}