package com.example.profile.impl
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _userName = MutableStateFlow("John Doe")
    val userName: StateFlow<String> = _userName
    private val _userEmail = MutableStateFlow("john.doe@example.com")
    val userEmail: StateFlow<String> = _userEmail
}
