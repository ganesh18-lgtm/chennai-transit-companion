package com.urbantransit.offline.domain.model

data class UserSession(
    val id: String,
    val identifier: String, // Mobile number (e.g. +91 98765 43210) or Email
    val displayName: String,
    val isLoggedIn: Boolean,
    val isGuest: Boolean = false,
    val metroCardNumber: String? = "CMRL-8829-4102",
    val walletBalanceCents: Long = 25000L, // ₹250.00 initial smartcard balance
    val email: String = "commuter.chennai@transit.in",
    val phoneNumber: String = "+91 98400 12345",
    val address: String = "No. 42, 2nd Avenue, Anna Nagar, Chennai - 600040",
    val profilePhotoUri: String? = null,
    val avatarEmoji: String = "🚇",
    val emergencyContact: String = "+91 94440 98765",
    val concessionCategory: String = "General Commuter"
)
