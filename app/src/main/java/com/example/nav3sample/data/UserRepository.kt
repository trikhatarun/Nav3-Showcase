package com.example.nav3sample.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    fun isUserLoggedIn(): Boolean {
        // TODO: Implement actual login check logic
        return false
    }
}

