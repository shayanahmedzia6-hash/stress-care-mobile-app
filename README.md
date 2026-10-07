# StressCare - Multiplatform Mobile System

This repository provides both **Native Android** and **React Native (Android + iOS)** implementations of the StressCare biometric stress monitoring & intervention application.

---

## 📁 Repository Structure

| Directory | Platform | Framework / Tech Stack |
| :--- | :--- | :--- |
| **`/react-native-app`** | **Android + iOS** | **React Native / Expo (TypeScript, SVG, Audio, BLE)** |
| **`/app`** | **Native Android** | **Kotlin, Jetpack Compose, Material 3, Room, BLE** |

---

## 📱 1. React Native (Android + iOS) - `/react-native-app`

The complete cross-platform React Native source code is located in the **`react-native-app/`** folder. It is designed to run seamlessly on both iPhone (iOS) and Android devices.

### Quick Start:
```bash
cd react-native-app
npm install

# Run on iOS (Simulator or iPhone via Expo Go)
npm run ios
# or: npm run start

# Run on Android (Emulator or Phone)
npm run android
```

### Key Modules:
- **`App.tsx`**: Main application coordinating all screens and overlays.
- **`src/screens/`**: Dashboard, Monitoring, Analytics, Interventions, Profile.
- **`src/overlays/`**: BLE Device Pairing, 60s Sensor Calibration, 4-4-4-4 Box Breathing, Binaural Audio Player, PSS-10 Questionnaire, Migraine Risk Model, Notifications, Feedback, Auth, and Onboarding.
- **`src/theme/colors.ts`**: Pure pastel light mode (`#deeeff`, `#f5f8ff`, `#ffffff`) and modern dark mode.
- **`ios/`**: Podfile and Info.plist preconfigured for iOS Bluetooth and Audio background modes.

---

## 🤖 2. Native Android - `/app`
The native Android codebase powers the instant live browser emulator within AI Studio:
- Built with Kotlin and Jetpack Compose.
- Gradle build configuration via `build.gradle.kts`.
