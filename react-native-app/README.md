# StressCare Mobile (React Native - Android & iOS)

A cross-platform React Native application for real-time stress detection, wearable bio-telemetry streaming, box breathing interventions, and migraine forecasting.

## 📱 Supported Platforms
- **iOS** (iPhone & iPad - iOS 13.4+)
- **Android** (Android 8.0+ / API 26+)
- **Web** (Expo Web preview)

---

## 🚀 Quick Start (Expo / React Native)

### 1. Install Dependencies
```bash
cd react-native-app
npm install
```

### 2. Run on iOS (Simulator or Physical iPhone)
```bash
# Using Expo Go (Scan QR code with iPhone Camera):
npm run start

# Or compile native iOS app (Requires macOS + Xcode):
npx pod-install
npm run ios
```

### 3. Run on Android (Emulator or Physical Android Device)
```bash
# Using Expo Go:
npm run start

# Or compile native Android APK/Debug build:
npm run android
```

---

## 📂 Project Architecture

```
react-native-app/
├── App.tsx                     # Root application container & modal coordinator
├── app.json                    # Expo, iOS (bundleId) & Android (package) configurations
├── package.json                # React Native & Expo cross-platform dependencies
├── tsconfig.json               # TypeScript strict configuration
├── ios/
│   ├── Podfile                 # CocoaPods configuration for iOS
│   └── Info.plist              # Bluetooth, Audio, and Background mode permissions
└── src/
    ├── types/                  # Biometric telemetry & clinical model types
    ├── theme/                  # Light (Pastel Sky Blue) & Dark mode color systems
    ├── context/                # Global state (BLE stream, timer, audio, scoring)
    ├── components/
    │   ├── Header.tsx          # App bar with band battery & dark mode switch
    │   ├── BottomNavBar.tsx    # 5-tab navigation bar
    │   ├── CircularGauge.tsx   # SVG animated stress score dial (0-100)
    │   └── SparklineChart.tsx  # Waveforms for PPG, GSR, and Skin Temperature
    ├── screens/
    │   ├── DashboardScreen.tsx # Bio-feedback gauge, metrics & quick relief
    │   ├── MonitoringScreen.tsx# Continuous waveforms & sensor toggles
    │   ├── AnalyticsScreen.tsx # Trends, diurnal distribution & sleep recovery
    │   ├── InterventionsScreen.tsx # Box breathing, binaural soundscapes, PMR
    │   └── ProfileScreen.tsx   # Wearable connection, calibration & cloud sync
    └── overlays/
        ├── BlePairingModal.tsx # BLE band scanning & pairing
        ├── CalibrationModal.tsx# 60-second baseline collection
        ├── GuidedBreathingOverlay.tsx # 4-4-4-4 Box Breathing visualizer
        ├── AudioPlayerOverlay.tsx # Binaural beats & sound therapy
        ├── PssQuestionnaireOverlay.tsx # Clinical PSS-10 assessment form
        ├── MigraineRiskOverlay.tsx # AI 24-hr migraine forecast
        ├── NotificationsOverlay.tsx # Stress spikes & alert history
        ├── FeedbackOverlay.tsx # Clinical feedback submission
        ├── AuthOverlay.tsx     # Medical cloud account login
        └── OnboardingOverlay.tsx # 4-step feature tour
```

---

## 🛠 Features Included
- **Continuous Bio-Telemetry Simulation & Real BLE Ready**: Real-time Heart Rate (BPM), HRV (RMSSD), Galvanic Skin Response (GSR in $\mu$S), and Skin Temperature ($^\circ$C).
- **Light & Dark Theme Matching**: Crisp pastel sky blue (#deeeff, #f5f8ff, #1a2b4e) and dark navy mode.
- **Vagus Nerve Interventions**: Interactive 4-4-4-4 Box Breathing with animated circle scaling and 432 Hz / 528 Hz soundscapes.
- **Clinical PSS-10 Scale**: Standard Perceived Stress Scale with reverse scoring and clinical risk categorization.
- **Export & Build**: Ready for standalone release via `eas build --platform ios` and `eas build --platform android`.
