package com.urbantransit.offline.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.urbantransit.offline.data.local.ChennaiTransitSeed
import com.urbantransit.offline.location.GeofenceManager
import com.urbantransit.offline.presentation.ui.screens.AuthScreen
import com.urbantransit.offline.presentation.ui.screens.FareBreakdownScreen
import com.urbantransit.offline.presentation.ui.screens.HomeScreen
import com.urbantransit.offline.presentation.ui.screens.MtcBusDirectoryScreen
import com.urbantransit.offline.presentation.ui.screens.NetworkMapScreen
import com.urbantransit.offline.presentation.ui.screens.ProfileEditScreen
import com.urbantransit.offline.presentation.ui.theme.ChennaiColors
import com.urbantransit.offline.presentation.ui.theme.ChennaiTransitTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TransitViewModel by viewModels()

    @Inject
    lateinit var geofenceManager: GeofenceManager

    private val foregroundPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        if (fineLocationGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            geofenceManager.getBackgroundLocationPermission()?.let { bgPermission ->
                backgroundPermissionLauncher.launch(bgPermission)
            }
        }
    }

    private val backgroundPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleWidgetIntent(intent)
        requestRequiredPermissions()

        setContent {
            ChennaiTransitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = ChennaiColors.DeepCanvas
                ) {
                    val currentScreen by viewModel.currentScreen.collectAsState()
                    val uiState by viewModel.uiState.collectAsState()
                    val session by viewModel.session.collectAsState()

                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "ScreenTransition"
                    ) { targetScreen ->
                        when (targetScreen) {
                            AppScreen.AUTH -> {
                                AuthScreen(
                                    onLoginSuccess = { identifier ->
                                        viewModel.login(identifier)
                                    },
                                    onGuestLogin = {
                                        viewModel.loginAsGuest()
                                    }
                                )
                            }
                            AppScreen.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    uiState = uiState,
                                    session = session,
                                    onNavigateToFareDetails = {
                                        viewModel.navigateTo(AppScreen.FARE_DETAILS)
                                    },
                                    onNavigateToProfile = {
                                        viewModel.navigateTo(AppScreen.PROFILE)
                                    },
                                    onNavigateToMap = {
                                        viewModel.navigateTo(AppScreen.NETWORK_MAP)
                                    },
                                    onNavigateToMtcDirectory = {
                                        viewModel.navigateTo(AppScreen.MTC_DIRECTORY)
                                    }
                                )
                            }
                            AppScreen.FARE_DETAILS -> {
                                FareBreakdownScreen(
                                    route = uiState.selectedRoute,
                                    originStop = uiState.selectedOriginStop,
                                    destinationStop = uiState.selectedDestinationStop,
                                    fare = uiState.calculatedFare,
                                    onBackClick = {
                                        viewModel.navigateTo(AppScreen.HOME)
                                    },
                                    onPurchaseOfflineTicket = {
                                        viewModel.purchaseOfflineTicket()
                                    }
                                )
                            }
                            AppScreen.PROFILE -> {
                                ProfileEditScreen(
                                    session = session,
                                    onBackClick = {
                                        viewModel.navigateTo(AppScreen.HOME)
                                    },
                                    onSaveProfile = { name, email, phone, address, metroCard, emergency, avatar, concession ->
                                        viewModel.updateUserProfile(
                                            name = name,
                                            email = email,
                                            phoneNumber = phone,
                                            address = address,
                                            metroCardNumber = metroCard,
                                            emergencyContact = emergency,
                                            avatarEmoji = avatar,
                                            concessionCategory = concession
                                        )
                                        viewModel.navigateTo(AppScreen.HOME)
                                    }
                                )
                            }
                            AppScreen.NETWORK_MAP -> {
                                NetworkMapScreen(
                                    routes = uiState.routes,
                                    allStops = if (uiState.allStops.isNotEmpty()) uiState.allStops else ChennaiTransitSeed.stops,
                                    onBackClick = {
                                        viewModel.navigateTo(AppScreen.HOME)
                                    },
                                    onSelectStopForJourney = { stop, isOrigin ->
                                        if (isOrigin) {
                                            viewModel.onOriginStopSelected(stop)
                                        } else {
                                            viewModel.onDestinationStopSelected(stop)
                                        }
                                        viewModel.navigateTo(AppScreen.HOME)
                                    }
                                )
                            }
                            AppScreen.MTC_DIRECTORY -> {
                                MtcBusDirectoryScreen(
                                    onBackClick = {
                                        viewModel.navigateTo(AppScreen.HOME)
                                    },
                                    onSelectBusRoute = { busRoute ->
                                        val matching = uiState.routes.find {
                                            it.shortName.startsWith(busRoute.routeNumber, ignoreCase = true)
                                        }
                                        if (matching != null) {
                                            viewModel.onRouteSelected(matching)
                                        }
                                        viewModel.navigateTo(AppScreen.HOME)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleWidgetIntent(intent)
    }

    private fun handleWidgetIntent(intent: android.content.Intent?) {
        val target = intent?.getStringExtra(com.urbantransit.offline.widget.ChennaiTransitAppWidgetProvider.EXTRA_START_SCREEN)
        when (target) {
            "FARE" -> viewModel.navigateTo(AppScreen.FARE_DETAILS)
            "MTC_DIRECTORY" -> viewModel.navigateTo(AppScreen.MTC_DIRECTORY)
            "HOME" -> viewModel.navigateTo(AppScreen.HOME)
        }
    }

    private fun requestRequiredPermissions() {
        if (!geofenceManager.hasForegroundLocationPermission()) {
            foregroundPermissionsLauncher.launch(
                geofenceManager.getRequiredForegroundPermissions()
            )
        } else if (!geofenceManager.hasBackgroundLocationPermission()) {
            geofenceManager.getBackgroundLocationPermission()?.let { bgPermission ->
                backgroundPermissionLauncher.launch(bgPermission)
            }
        }
    }
}
