package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BottomNavBar
import com.example.ui.overlays.AudioPlayerOverlay
import com.example.ui.overlays.AuthOverlay
import com.example.ui.overlays.BlePairingModal
import com.example.ui.overlays.CalibrationModal
import com.example.ui.overlays.FeedbackOverlay
import com.example.ui.overlays.GuidedBreathingOverlay
import com.example.ui.overlays.MigraineRiskOverlay
import com.example.ui.overlays.NotificationsOverlay
import com.example.ui.overlays.OnboardingOverlay
import com.example.ui.overlays.PssQuestionnaireOverlay
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.InterventionsScreen
import com.example.ui.screens.MonitoringScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ActiveOverlay
import com.example.viewmodel.MainTab
import com.example.viewmodel.StressCareViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: StressCareViewModel = viewModel()
            val isDarkMode by vm.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDarkMode) {
                StressCareApp(viewModel = vm)
            }
        }
    }
}

@Composable
fun StressCareApp(viewModel: StressCareViewModel = viewModel()) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeOverlay by viewModel.activeOverlay.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Handle back press gracefully: if an overlay is open, close it; if not on dashboard, return to dashboard
    BackHandler(enabled = activeOverlay != ActiveOverlay.NONE || currentTab != MainTab.DASHBOARD) {
        if (activeOverlay != ActiveOverlay.NONE) {
            viewModel.closeOverlay()
        } else if (currentTab != MainTab.DASHBOARD) {
            viewModel.selectTab(MainTab.DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (activeOverlay == ActiveOverlay.NONE) {
                BottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Tab Navigation with smooth cross-fade animation
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_navigation"
            ) { tab ->
                when (tab) {
                    MainTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                    MainTab.MONITORING -> MonitoringScreen(viewModel = viewModel)
                    MainTab.ANALYTICS -> AnalyticsScreen(viewModel = viewModel)
                    MainTab.INTERVENTIONS -> InterventionsScreen(viewModel = viewModel)
                    MainTab.PROFILE -> ProfileScreen(viewModel = viewModel)
                }
            }

            // Fullscreen & Modal Overlays
            when (activeOverlay) {
                ActiveOverlay.ONBOARDING -> {
                    OnboardingOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.AUTH -> {
                    AuthOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.BLE_PAIRING -> {
                    BlePairingModal(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.CALIBRATION -> {
                    CalibrationModal(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.GUIDED_BREATHING -> {
                    GuidedBreathingOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.AUDIO_PLAYER -> {
                    AudioPlayerOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.PSS_QUESTIONNAIRE -> {
                    PssQuestionnaireOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.MIGRAINE_DETAIL -> {
                    MigraineRiskOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.NOTIFICATIONS -> {
                    NotificationsOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.FEEDBACK -> {
                    FeedbackOverlay(
                        viewModel = viewModel,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                ActiveOverlay.NONE -> {
                    // No modal active
                }
            }
        }
    }
}
