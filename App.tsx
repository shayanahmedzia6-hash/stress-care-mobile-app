import React from 'react';
import { StyleSheet, View, Text, Platform, ActivityIndicator } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';
import { StatusBar } from 'expo-status-bar';
import { LinearGradient } from 'expo-linear-gradient';

import { StressCareProvider, useStressCare } from './src/context/StressCareContext';
import { LightColors, DarkColors } from './src/theme/colors';

import { Header } from './src/components/Header';
import { BottomNavBar } from './src/components/BottomNavBar';

import { OnboardingScreen } from './src/screens/OnboardingScreen';
import { AuthScreen } from './src/screens/AuthScreen';
import { DashboardScreen } from './src/screens/DashboardScreen';
import { MonitoringScreen } from './src/screens/MonitoringScreen';
import { AnalyticsScreen } from './src/screens/AnalyticsScreen';
import { InterventionsScreen } from './src/screens/InterventionsScreen';
import { ProfileScreen } from './src/screens/ProfileScreen';

import { BlePairingModal } from './src/overlays/BlePairingModal';
import { CalibrationModal } from './src/overlays/CalibrationModal';
import { GuidedBreathingOverlay } from './src/overlays/GuidedBreathingOverlay';
import { AudioPlayerOverlay } from './src/overlays/AudioPlayerOverlay';
import { PssQuestionnaireOverlay } from './src/overlays/PssQuestionnaireOverlay';
import { MigraineRiskOverlay } from './src/overlays/MigraineRiskOverlay';
import { NotificationsOverlay } from './src/overlays/NotificationsOverlay';
import { FeedbackOverlay } from './src/overlays/FeedbackOverlay';
import { AuthOverlay } from './src/overlays/AuthOverlay';
import { OnboardingOverlay } from './src/overlays/OnboardingOverlay';

const MainAppContent: React.FC = () => {
  const { currentTab, isDarkMode, toastMessage } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <LinearGradient colors={colors.backgroundGradient} style={styles.gradientContainer}>
      <StatusBar style={isDarkMode ? 'light' : 'dark'} />
      <SafeAreaView style={styles.safeArea} edges={['top', 'left', 'right']}>
        {toastMessage ? (
          <View style={[styles.toastBanner, { backgroundColor: colors.primary }]}>
            <Text style={styles.toastText}>{toastMessage}</Text>
          </View>
        ) : null}

        <Header />

        <View style={styles.screenContainer}>
          {currentTab === 'DASHBOARD' && <DashboardScreen />}
          {currentTab === 'MONITORING' && <MonitoringScreen />}
          {currentTab === 'ANALYTICS' && <AnalyticsScreen />}
          {currentTab === 'INTERVENTIONS' && <InterventionsScreen />}
          {currentTab === 'PROFILE' && <ProfileScreen />}
        </View>

        <BottomNavBar />

        <BlePairingModal />
        <CalibrationModal />
        <GuidedBreathingOverlay />
        <AudioPlayerOverlay />
        <PssQuestionnaireOverlay />
        <MigraineRiskOverlay />
        <NotificationsOverlay />
        <FeedbackOverlay />
        <AuthOverlay />
        <OnboardingOverlay />
      </SafeAreaView>
    </LinearGradient>
  );
};

const RootNavigator: React.FC = () => {
  const { appPhase, isHydrated, isDarkMode } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  if (!isHydrated) {
    return (
      <View style={[styles.loadingWrap, { backgroundColor: colors.backgroundGradient[0] }]}>
        <ActivityIndicator size="large" color={colors.primary} />
        <Text style={[styles.loadingText, { color: colors.textSecondary }]}>StressCare</Text>
      </View>
    );
  }

  if (appPhase === 'onboarding') {
    return (
      <>
        <StatusBar style={isDarkMode ? 'light' : 'dark'} />
        <OnboardingScreen />
      </>
    );
  }

  if (appPhase === 'auth') {
    return (
      <>
        <StatusBar style={isDarkMode ? 'light' : 'dark'} />
        <AuthScreen />
      </>
    );
  }

  return <MainAppContent />;
};

export default function App() {
  return (
    <SafeAreaProvider>
      <StressCareProvider>
        <RootNavigator />
      </StressCareProvider>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  gradientContainer: {
    flex: 1,
  },
  safeArea: {
    flex: 1,
  },
  screenContainer: {
    flex: 1,
  },
  loadingWrap: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 12,
  },
  loadingText: {
    fontSize: 16,
    fontWeight: '700',
  },
  toastBanner: {
    position: 'absolute',
    top: Platform.OS === 'ios' ? 50 : 20,
    left: 20,
    right: 20,
    zIndex: 9999,
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderRadius: 14,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 6,
    alignItems: 'center',
  },
  toastText: {
    color: '#ffffff',
    fontSize: 13,
    fontWeight: '700',
    textAlign: 'center',
  },
});
