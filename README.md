# StressCare Mobile (React Native / Expo)

Cross-platform StressCare app for wearable stress monitoring, interventions, and analytics.

## App flow
1. **Onboarding** (Skip / Next)
2. **Login / Sign up**
3. **Main app** (Dashboard, Monitoring, Analytics, Interventions, Profile)

## Setup
```bash
npm install
npx expo start
```

### Run with Expo Go (recommended for demo)
```bash
npx expo start
# Scan QR with Expo Go on Android phone
```

### Android APK (local)
> Windows tip: folder path mein spaces (`stresscare mobile app`) CMake break kar sakte hain.
> Space-free junction se build karo: `C:\stresscare-rn` → yeh project.

```bash
npx expo prebuild --platform android
# from C:\stresscare-rn\android (junction without spaces):
.\gradlew.bat assembleDebug
# APK: android/app/build/outputs/apk/debug/app-debug.apk
# also copied to: builds/StressCare-debug.apk
```

### Android APK (EAS cloud — no local NDK needed)
```bash
npx eas-cli login
npx eas-cli build --platform android --profile preview
```

## Cloud CI (Codemagic / Appcircle)
- Project type: **React Native** / **Expo**
- Project path: `/` (repo root is the RN app)
- Requires `package.json`, `app.json`, `index.js`, and `android/` (already generated)

## Structure
```
├── App.tsx                 # Root navigator (onboarding → auth → main)
├── index.js                # Expo entry
├── app.json                # Expo config
├── src/
│   ├── screens/            # Onboarding, Auth, Dashboard, ...
│   ├── overlays/           # BLE, breathing, PSS, etc.
│   ├── components/
│   ├── context/
│   ├── theme/
│   └── types/
└── assets/
```

> The original Kotlin Android project lives outside this repo at  
> `../kotlin app/` (sibling folder under `stresscare mobile app`).
