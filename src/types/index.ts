export type MainTab =
  | 'DASHBOARD'
  | 'MONITORING'
  | 'ANALYTICS'
  | 'INTERVENTIONS'
  | 'PROFILE';

export type AppPhase = 'onboarding' | 'auth' | 'main';

export type ActiveOverlay =
  | 'NONE'
  | 'BLE_PAIRING'
  | 'CALIBRATION'
  | 'GUIDED_BREATHING'
  | 'AUDIO_PLAYER'
  | 'PSS_QUESTIONNAIRE'
  | 'MIGRAINE_DETAIL'
  | 'NOTIFICATIONS'
  | 'FEEDBACK'
  | 'AUTH'
  | 'ONBOARDING';

export type StressCategory = 'LOW' | 'NORMAL' | 'ELEVATED' | 'HIGH';

export interface LiveVitals {
  heartRate: number;
  hrvRmssd: number;
  gsrMicrosiemens: number;
  skinTempCelsius: number;
  accelMagnitude: number;
  stressScore: number;
  stressCategory: StressCategory;
  batteryPercent: number;
  isBandConnected: boolean;
  deviceName: string;
}

export interface CalibrationStep {
  title: string;
  description: string;
  isCompleted: boolean;
  isInProgress: boolean;
  value: string;
}

export type BreathingPhaseType = 'INHALE' | 'HOLD_IN' | 'EXHALE' | 'HOLD_OUT';

export interface BreathingPhaseConfig {
  type: BreathingPhaseType;
  label: string;
  seconds: number;
}

export interface PssResult {
  score: number;
  category: string;
  mood: string;
  notes: string;
  timestamp: number;
}

export interface DiscoveredDevice {
  id: string;
  name: string;
  rssi: number;
  batteryLevel: number;
}
