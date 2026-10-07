import React, { createContext, useContext, useState, useEffect, useRef } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {
  MainTab,
  AppPhase,
  ActiveOverlay,
  LiveVitals,
  CalibrationStep,
  BreathingPhaseConfig,
  PssResult,
} from '../types';

const STORAGE_KEYS = {
  onboarding: '@stresscare/onboarding_done',
  auth: '@stresscare/authenticated',
  userName: '@stresscare/user_name',
};

interface StressCareContextType {
  // App flow: Onboarding → Auth → Main
  appPhase: AppPhase;
  isHydrated: boolean;
  userName: string;
  completeOnboarding: () => void;
  completeAuth: (name?: string) => void;
  logout: () => void;

  // Navigation & Overlays
  currentTab: MainTab;
  setCurrentTab: (tab: MainTab) => void;
  activeOverlay: ActiveOverlay;
  openOverlay: (overlay: ActiveOverlay) => void;
  closeOverlay: () => void;

  // Theming
  isDarkMode: boolean;
  toggleDarkMode: () => void;

  // Live Vitals
  vitals: LiveVitals;
  historyPoints: { time: string; stress: number; hr: number; hrv: number }[];
  toggleBandConnection: () => void;

  // Calibration
  calibrationProgress: number;
  calibrationRemainingSeconds: number;
  calibrationSteps: CalibrationStep[];
  startCalibration: () => void;
  cancelCalibration: () => void;

  // Guided Breathing
  breathingActive: boolean;
  breathingPhase: BreathingPhaseConfig;
  breathingPhaseProgress: number;
  breathingCycleCount: number;
  breathingRemainingSeconds: number;
  startBreathing: () => void;
  stopBreathing: () => void;

  // Audio Player
  audioPlaying: boolean;
  currentSoundtrack: string;
  soundtracks: string[];
  audioVolume: number;
  toggleAudioPlay: () => void;
  selectSoundtrack: (name: string) => void;
  setAudioVolume: (vol: number) => void;

  // PSS-10
  pssResults: PssResult[];
  submitPss: (answers: Record<number, number>, mood: string, notes: string) => PssResult;

  // Cloud Sync
  isSyncing: boolean;
  triggerSync: () => Promise<void>;

  // Notification / Toast
  toastMessage: string | null;
  showToast: (msg: string) => void;
}

const defaultVitals: LiveVitals = {
  heartRate: 74,
  hrvRmssd: 58,
  gsrMicrosiemens: 2.4,
  skinTempCelsius: 34.2,
  accelMagnitude: 0.98,
  stressScore: 32,
  stressCategory: 'NORMAL',
  batteryPercent: 88,
  isBandConnected: true,
  deviceName: 'StressCare Band X1',
};

const breathingPhases: BreathingPhaseConfig[] = [
  { type: 'INHALE', label: 'Inhale deeply through nose...', seconds: 4 },
  { type: 'HOLD_IN', label: 'Hold breath gently...', seconds: 4 },
  { type: 'EXHALE', label: 'Exhale slowly through mouth...', seconds: 4 },
  { type: 'HOLD_OUT', label: 'Rest & pause calmly...', seconds: 4 },
];

const StressCareContext = createContext<StressCareContextType | undefined>(undefined);

export const StressCareProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [appPhase, setAppPhase] = useState<AppPhase>('onboarding');
  const [isHydrated, setIsHydrated] = useState(false);
  const [userName, setUserName] = useState('Alex Morgan');
  const [currentTab, setCurrentTab] = useState<MainTab>('DASHBOARD');
  const [activeOverlay, setActiveOverlay] = useState<ActiveOverlay>('NONE');
  const [isDarkMode, setIsDarkMode] = useState<boolean>(false);
  const [vitals, setVitals] = useState<LiveVitals>(defaultVitals);
  const [historyPoints, setHistoryPoints] = useState([
    { time: '09:00', stress: 28, hr: 70, hrv: 62 },
    { time: '10:00', stress: 35, hr: 75, hrv: 55 },
    { time: '11:00', stress: 62, hr: 88, hrv: 38 },
    { time: '12:00', stress: 45, hr: 78, hrv: 48 },
    { time: '13:00', stress: 30, hr: 72, hrv: 60 },
    { time: '14:00', stress: 50, hr: 80, hrv: 45 },
    { time: '15:00', stress: 32, hr: 74, hrv: 58 },
  ]);

  // Toast
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  // Restore onboarding / auth gate
  useEffect(() => {
    (async () => {
      try {
        const [onboardingDone, authenticated, savedName] = await Promise.all([
          AsyncStorage.getItem(STORAGE_KEYS.onboarding),
          AsyncStorage.getItem(STORAGE_KEYS.auth),
          AsyncStorage.getItem(STORAGE_KEYS.userName),
        ]);
        if (savedName) setUserName(savedName);
        if (authenticated === '1') {
          setAppPhase('main');
        } else if (onboardingDone === '1') {
          setAppPhase('auth');
        } else {
          setAppPhase('onboarding');
        }
      } catch {
        setAppPhase('onboarding');
      } finally {
        setIsHydrated(true);
      }
    })();
  }, []);

  const completeOnboarding = () => {
    AsyncStorage.setItem(STORAGE_KEYS.onboarding, '1').catch(() => {});
    setAppPhase('auth');
  };

  const completeAuth = (name?: string) => {
    const finalName = name?.trim() || userName || 'Alex Morgan';
    setUserName(finalName);
    AsyncStorage.multiSet([
      [STORAGE_KEYS.onboarding, '1'],
      [STORAGE_KEYS.auth, '1'],
      [STORAGE_KEYS.userName, finalName],
    ]).catch(() => {});
    setAppPhase('main');
    setActiveOverlay('NONE');
  };

  const logout = () => {
    AsyncStorage.multiSet([
      [STORAGE_KEYS.auth, '0'],
    ]).catch(() => {});
    setAppPhase('auth');
    setCurrentTab('DASHBOARD');
    setActiveOverlay('NONE');
    showToast('Signed out successfully');
  };

  // Continuous Telemetry simulation
  useEffect(() => {
    const interval = setInterval(() => {
      setVitals((prev) => {
        if (!prev.isBandConnected) return prev;
        const hrDrift = (Math.random() - 0.5) * 2;
        const newHr = Math.round(Math.min(130, Math.max(55, prev.heartRate + hrDrift)));
        const gsrDrift = (Math.random() - 0.5) * 0.1;
        const newGsr = parseFloat(Math.min(10, Math.max(0.5, prev.gsrMicrosiemens + gsrDrift)).toFixed(2));
        const tempDrift = (Math.random() - 0.5) * 0.05;
        const newTemp = parseFloat(Math.min(37.5, Math.max(32.0, prev.skinTempCelsius + tempDrift)).toFixed(1));
        const newHrv = Math.round(Math.min(95, Math.max(20, 100 - newHr * 0.6)));

        // Stress formula
        const rawStress = Math.round(newHr * 0.4 + (newGsr / 5) * 30 + (80 - newHrv) * 0.3);
        const score = Math.min(100, Math.max(5, rawStress));
        const cat = score > 65 ? 'HIGH' : score > 40 ? 'ELEVATED' : 'NORMAL';

        return {
          ...prev,
          heartRate: newHr,
          hrvRmssd: newHrv,
          gsrMicrosiemens: newGsr,
          skinTempCelsius: newTemp,
          stressScore: score,
          stressCategory: cat,
        };
      });
    }, 2000);
    return () => clearInterval(interval);
  }, []);

  // Calibration state
  const [calibrationProgress, setCalibrationProgress] = useState(0);
  const [calibrationRemainingSeconds, setCalibrationRemainingSeconds] = useState(60);
  const [calibrationSteps, setCalibrationSteps] = useState<CalibrationStep[]>([
    { title: 'Resting Heart Rate', description: 'Sitting still for 20 seconds', isCompleted: false, isInProgress: false, value: '-- bpm' },
    { title: 'Electrodermal Tonic Skin Conductance', description: 'Skin electrode stabilization', isCompleted: false, isInProgress: false, value: '-- uS' },
    { title: 'Peripheral Skin Temperature', description: 'Thermal equilibrium check', isCompleted: false, isInProgress: false, value: '-- °C' },
  ]);
  const calTimerRef = useRef<NodeJS.Timeout | null>(null);

  const startCalibration = () => {
    setCalibrationProgress(0);
    setCalibrationRemainingSeconds(60);
    setCalibrationSteps((steps) =>
      steps.map((s, idx) => ({
        ...s,
        isCompleted: false,
        isInProgress: idx === 0,
        value: idx === 0 ? 'Sampling...' : '--',
      }))
    );

    let sec = 60;
    if (calTimerRef.current) clearInterval(calTimerRef.current);
    calTimerRef.current = setInterval(() => {
      sec -= 1;
      setCalibrationRemainingSeconds(sec);
      const prog = (60 - sec) / 60;
      setCalibrationProgress(prog);

      if (sec === 40) {
        setCalibrationSteps((prev) => [
          { ...prev[0], isCompleted: true, isInProgress: false, value: '68 bpm (Calibrated)' },
          { ...prev[1], isInProgress: true, value: 'Sampling...' },
          prev[2],
        ]);
      } else if (sec === 20) {
        setCalibrationSteps((prev) => [
          prev[0],
          { ...prev[1], isCompleted: true, isInProgress: false, value: '1.8 uS (Calibrated)' },
          { ...prev[2], isInProgress: true, value: 'Sampling...' },
        ]);
      } else if (sec <= 0) {
        if (calTimerRef.current) clearInterval(calTimerRef.current);
        setCalibrationSteps((prev) => [
          prev[0],
          prev[1],
          { ...prev[2], isCompleted: true, isInProgress: false, value: '34.2 °C (Calibrated)' },
        ]);
        showToast('Baseline Calibration Completed Successfully!');
      }
    }, 1000);
  };

  const cancelCalibration = () => {
    if (calTimerRef.current) clearInterval(calTimerRef.current);
    setCalibrationRemainingSeconds(60);
    setCalibrationProgress(0);
  };

  // Breathing state
  const [breathingActive, setBreathingActive] = useState(false);
  const [breathingPhaseIndex, setBreathingPhaseIndex] = useState(0);
  const [breathingPhaseProgress, setBreathingPhaseProgress] = useState(0);
  const [breathingCycleCount, setBreathingCycleCount] = useState(1);
  const [breathingRemainingSeconds, setBreathingRemainingSeconds] = useState(120);
  const breathTimerRef = useRef<NodeJS.Timeout | null>(null);

  const startBreathing = () => {
    setBreathingActive(true);
    setBreathingPhaseIndex(0);
    setBreathingPhaseProgress(0);
    setBreathingCycleCount(1);
    setBreathingRemainingSeconds(120);

    let currentPhase = 0;
    let phaseTick = 0;
    let cycle = 1;
    let totalSec = 120;

    if (breathTimerRef.current) clearInterval(breathTimerRef.current);
    breathTimerRef.current = setInterval(() => {
      totalSec -= 1;
      setBreathingRemainingSeconds(totalSec);
      phaseTick += 0.25;

      const dur = breathingPhases[currentPhase].seconds;
      setBreathingPhaseProgress(Math.min(1, phaseTick / dur));

      if (phaseTick >= dur) {
        phaseTick = 0;
        currentPhase = (currentPhase + 1) % breathingPhases.length;
        setBreathingPhaseIndex(currentPhase);
        if (currentPhase === 0) {
          cycle += 1;
          setBreathingCycleCount(cycle);
        }
      }

      if (totalSec <= 0) {
        stopBreathing();
        showToast('2-Minute Guided Box Breathing Session Finished!');
      }
    }, 250);
  };

  const stopBreathing = () => {
    setBreathingActive(false);
    if (breathTimerRef.current) clearInterval(breathTimerRef.current);
  };

  // Audio player state
  const soundtracks = [
    '432 Hz Healing Resonance',
    '528 Hz Deep Stress Release',
    'Delta Wave Restorative Sleep',
    'Forest Rain & Flowing Stream',
    'Ocean Tide Alpha Rhythm',
  ];
  const [audioPlaying, setAudioPlaying] = useState(false);
  const [currentSoundtrack, setCurrentSoundtrack] = useState(soundtracks[0]);
  const [audioVolume, setAudioVolume] = useState(0.75);

  const toggleAudioPlay = () => {
    setAudioPlaying(!audioPlaying);
  };

  const selectSoundtrack = (name: string) => {
    setCurrentSoundtrack(name);
    setAudioPlaying(true);
  };

  // PSS-10
  const [pssResults, setPssResults] = useState<PssResult[]>([
    { score: 14, category: 'LOW', timestamp: Date.now() - 86400000 * 3, mood: 'Good', notes: '' },
  ]);

  const submitPss = (answers: Record<number, number>, mood: string, notes: string): PssResult => {
    // Reverse scored questions: 4, 5, 7, 8 (1-indexed)
    const reverse = [4, 5, 7, 8];
    let total = 0;
    for (let i = 1; i <= 10; i++) {
      const ans = answers[i] ?? 2;
      const score = reverse.includes(i) ? 4 - ans : ans;
      total += score;
    }
    const cat: 'LOW' | 'MODERATE' | 'HIGH' = total >= 27 ? 'HIGH' : total >= 14 ? 'MODERATE' : 'LOW';
    const result: PssResult = { score: total, category: cat, timestamp: Date.now(), notes, mood };
    setPssResults((prev) => [result, ...prev]);
    showToast(`PSS-10 assessment submitted: Score ${total} (${cat} stress)`);
    return result;
  };

  // Cloud Sync
  const [isSyncing, setIsSyncing] = useState(false);
  const triggerSync = async () => {
    setIsSyncing(true);
    await new Promise((resolve) => setTimeout(resolve, 1500));
    setIsSyncing(false);
    showToast('Biometric data synchronized with cloud storage successfully!');
  };

  const toggleBandConnection = () => {
    setVitals((prev) => ({
      ...prev,
      isBandConnected: !prev.isBandConnected,
    }));
    showToast(!vitals.isBandConnected ? 'Connected to StressCare Band X1' : 'Disconnected from Wearable');
  };

  const openOverlay = (overlay: ActiveOverlay) => {
    setActiveOverlay(overlay);
    if (overlay === 'CALIBRATION') startCalibration();
    if (overlay === 'GUIDED_BREATHING') startBreathing();
  };

  const closeOverlay = () => {
    if (activeOverlay === 'CALIBRATION') cancelCalibration();
    if (activeOverlay === 'GUIDED_BREATHING') stopBreathing();
    setActiveOverlay('NONE');
  };

  const toggleDarkMode = () => setIsDarkMode((prev) => !prev);

  return (
    <StressCareContext.Provider
      value={{
        appPhase,
        isHydrated,
        userName,
        completeOnboarding,
        completeAuth,
        logout,
        currentTab,
        setCurrentTab,
        activeOverlay,
        openOverlay,
        closeOverlay,
        isDarkMode,
        toggleDarkMode,
        vitals,
        historyPoints,
        toggleBandConnection,
        calibrationProgress,
        calibrationRemainingSeconds,
        calibrationSteps,
        startCalibration,
        cancelCalibration,
        breathingActive,
        breathingPhase: breathingPhases[breathingPhaseIndex],
        breathingPhaseProgress,
        breathingCycleCount,
        breathingRemainingSeconds,
        startBreathing,
        stopBreathing,
        audioPlaying,
        currentSoundtrack,
        soundtracks,
        audioVolume,
        toggleAudioPlay,
        selectSoundtrack,
        setAudioVolume,
        pssResults,
        submitPss,
        isSyncing,
        triggerSync,
        toastMessage,
        showToast,
      }}
    >
      {children}
    </StressCareContext.Provider>
  );
};

export const useStressCare = () => {
  const context = useContext(StressCareContext);
  if (!context) throw new Error('useStressCare must be used within StressCareProvider');
  return context;
};
