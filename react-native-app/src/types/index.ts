export type MainTab = 'DASHBOARD' | 'MONITORING' | 'ANALYTICS' | 'INTERVENTIONS' | 'PROFILE';

export type ActiveOverlay =
  | 'NONE'
  | 'ONBOARDING'
  | 'AUTH'
  | 'BLE_PAIRING'
  | 'CALIBRATION'
  | 'GUIDED_BREATHING'
  | 'AUDIO_PLAYER'
  | 'PSS_QUESTIONNAIRE'
  | 'MIGRAINE_DETAIL'
  | 'NOTIFICATIONS'
  | 'FEEDBACK';

export type StressCategory = 'NORMAL' | 'ELEVATED' | 'HIGH';

export interface LiveVitals {
  heartRate: number; // bpm
  hrvRmssd: number; // ms
  gsrMicrosiemens: number; // uS
  skinTempCelsius: number; // C
  accelMagnitude: number; // Gs
  stressScore: number; // 0-100
  stressCategory: StressCategory;
  batteryPercent: number;
  isBandConnected: boolean;
  deviceName: string;
}

export interface DiscoveredDevice {
  id: string;
  name: string;
  rssi: number;
  batteryLevel: number;
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
  category: 'LOW' | 'MODERATE' | 'HIGH';
  timestamp: number;
  notes?: string;
  mood?: string;
}

export interface HistoricalReading {
  timeLabel: string;
  stressScore: number;
  heartRate: number;
  hrv: number;
  gsr: number;
}

export interface MigraineRiskAssessment {
  riskScore: number; // 0-100%
  riskLevel: 'LOW' | 'MODERATE' | 'HIGH';
  factors: {
    name: string;
    impact: 'Low' | 'Moderate' | 'Severe';
    description: string;
  }[];
}
