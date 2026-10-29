package com.urbantransit.offline.data.auth

import android.content.Context
import android.content.SharedPreferences
import com.urbantransit.offline.domain.model.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE)

    private val _session = MutableStateFlow(loadInitialSession())
    val session: StateFlow<UserSession> = _session.asStateFlow()

    private fun loadInitialSession(): UserSession {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val identifier = prefs.getString(KEY_IDENTIFIER, "") ?: ""
        val displayName = prefs.getString(KEY_DISPLAY_NAME, "Chennai Commuter") ?: "Chennai Commuter"
        val isGuest = prefs.getBoolean(KEY_IS_GUEST, false)
        val email = prefs.getString(KEY_EMAIL, if (identifier.contains("@")) identifier else "commuter.chennai@transit.in") ?: "commuter.chennai@transit.in"
        val phone = prefs.getString(KEY_PHONE, if (!identifier.contains("@") && identifier.isNotBlank()) identifier else "+91 98400 12345") ?: "+91 98400 12345"
        val address = prefs.getString(KEY_ADDRESS, "No. 42, 2nd Avenue, Anna Nagar, Chennai - 600040") ?: "No. 42, 2nd Avenue, Anna Nagar, Chennai - 600040"
        val metroCard = prefs.getString(KEY_METRO_CARD, "CMRL-8829-4102") ?: "CMRL-8829-4102"
        val avatarEmoji = prefs.getString(KEY_AVATAR_EMOJI, "🚇") ?: "🚇"
        val emergencyContact = prefs.getString(KEY_EMERGENCY_CONTACT, "+91 94440 98765") ?: "+91 94440 98765"
        val concessionCategory = prefs.getString(KEY_CONCESSION, "General Commuter") ?: "General Commuter"
        val photoUri = prefs.getString(KEY_PHOTO_URI, null)

        return if (isLoggedIn) {
            UserSession(
                id = prefs.getString(KEY_USER_ID, UUID.randomUUID().toString()) ?: UUID.randomUUID().toString(),
                identifier = identifier,
                displayName = displayName,
                isLoggedIn = true,
                isGuest = isGuest,
                metroCardNumber = metroCard,
                walletBalanceCents = prefs.getLong(KEY_WALLET_BALANCE, 25000L),
                email = email,
                phoneNumber = phone,
                address = address,
                profilePhotoUri = photoUri,
                avatarEmoji = avatarEmoji,
                emergencyContact = emergencyContact,
                concessionCategory = concessionCategory
            )
        } else {
            UserSession(
                id = "",
                identifier = "",
                displayName = "Guest",
                isLoggedIn = false
            )
        }
    }

    fun loginWithMobileOrEmail(identifier: String, name: String = "Chennai Commuter"): Boolean {
        if (identifier.isBlank()) return false

        val isEmail = identifier.contains("@")
        val email = if (isEmail) identifier else prefs.getString(KEY_EMAIL, "commuter.chennai@transit.in") ?: "commuter.chennai@transit.in"
        val phone = if (!isEmail) identifier else prefs.getString(KEY_PHONE, "+91 98400 12345") ?: "+91 98400 12345"

        val userId = UUID.randomUUID().toString()
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, false)
            .putString(KEY_USER_ID, userId)
            .putString(KEY_IDENTIFIER, identifier)
            .putString(KEY_DISPLAY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_PHONE, phone)
            .putLong(KEY_WALLET_BALANCE, 32000L)
            .apply()

        _session.value = UserSession(
            id = userId,
            identifier = identifier,
            displayName = name,
            isLoggedIn = true,
            isGuest = false,
            metroCardNumber = prefs.getString(KEY_METRO_CARD, "CMRL-8829-4102"),
            walletBalanceCents = 32000L,
            email = email,
            phoneNumber = phone,
            address = prefs.getString(KEY_ADDRESS, "No. 42, 2nd Avenue, Anna Nagar, Chennai - 600040") ?: "No. 42, 2nd Avenue, Anna Nagar, Chennai - 600040",
            profilePhotoUri = prefs.getString(KEY_PHOTO_URI, null),
            avatarEmoji = prefs.getString(KEY_AVATAR_EMOJI, "🚇") ?: "🚇",
            emergencyContact = prefs.getString(KEY_EMERGENCY_CONTACT, "+91 94440 98765") ?: "+91 94440 98765",
            concessionCategory = prefs.getString(KEY_CONCESSION, "General Commuter") ?: "General Commuter"
        )
        return true
    }

    fun loginAsGuest() {
        val guestId = "guest_" + UUID.randomUUID().toString().take(8)
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, true)
            .putString(KEY_USER_ID, guestId)
            .putString(KEY_IDENTIFIER, "guest@chennaitransit.in")
            .putString(KEY_DISPLAY_NAME, "Guest Explorer")
            .putLong(KEY_WALLET_BALANCE, 10000L)
            .apply()

        _session.value = UserSession(
            id = guestId,
            identifier = "guest@chennaitransit.in",
            displayName = "Guest Explorer",
            isLoggedIn = true,
            isGuest = true,
            metroCardNumber = "CMRL-GUEST-001",
            walletBalanceCents = 10000L
        )
    }

    fun updateProfile(
        name: String,
        email: String,
        phoneNumber: String,
        address: String,
        metroCardNumber: String,
        emergencyContact: String,
        avatarEmoji: String,
        concessionCategory: String,
        photoUri: String? = null
    ) {
        prefs.edit()
            .putString(KEY_DISPLAY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_PHONE, phoneNumber)
            .putString(KEY_ADDRESS, address)
            .putString(KEY_METRO_CARD, metroCardNumber)
            .putString(KEY_EMERGENCY_CONTACT, emergencyContact)
            .putString(KEY_AVATAR_EMOJI, avatarEmoji)
            .putString(KEY_CONCESSION, concessionCategory)
            .apply()

        val current = _session.value
        _session.value = current.copy(
            displayName = name,
            email = email,
            phoneNumber = phoneNumber,
            address = address,
            metroCardNumber = metroCardNumber,
            emergencyContact = emergencyContact,
            avatarEmoji = avatarEmoji,
            concessionCategory = concessionCategory,
            profilePhotoUri = photoUri ?: current.profilePhotoUri
        )
    }

    fun logout() {
        prefs.edit().clear().apply()
        _session.value = UserSession(
            id = "",
            identifier = "",
            displayName = "Guest",
            isLoggedIn = false
        )
    }

    companion object {
        private const val PREFS_AUTH = "chennai_transit_auth_prefs"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val KEY_IS_GUEST = "key_is_guest"
        private const val KEY_USER_ID = "key_user_id"
        private const val KEY_IDENTIFIER = "key_identifier"
        private const val KEY_DISPLAY_NAME = "key_display_name"
        private const val KEY_WALLET_BALANCE = "key_wallet_balance"
        private const val KEY_EMAIL = "key_email"
        private const val KEY_PHONE = "key_phone"
        private const val KEY_ADDRESS = "key_address"
        private const val KEY_METRO_CARD = "key_metro_card"
        private const val KEY_AVATAR_EMOJI = "key_avatar_emoji"
        private const val KEY_EMERGENCY_CONTACT = "key_emergency_contact"
        private const val KEY_CONCESSION = "key_concession"
        private const val KEY_PHOTO_URI = "key_photo_uri"
    }
}
