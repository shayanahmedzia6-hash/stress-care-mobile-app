import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  Dimensions,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Feather, Ionicons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';

export const GuidedBreathingOverlay: React.FC = () => {
  const {
    activeOverlay,
    closeOverlay,
    breathingPhase,
    breathingPhaseProgress,
    breathingCycleCount,
    breathingRemainingSeconds,
    vitals,
  } = useStressCare();

  const minutes = Math.floor(breathingRemainingSeconds / 60);
  const seconds = breathingRemainingSeconds % 60;
  const timeFormatted = `${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;

  // Calculate dynamic scale for animated circle
  // Inhale: 1.0 -> 1.5
  // Hold in: 1.5
  // Exhale: 1.5 -> 1.0
  // Hold out: 1.0
  let scale = 1.0;
  if (breathingPhase.type === 'INHALE') {
    scale = 1.0 + breathingPhaseProgress * 0.45;
  } else if (breathingPhase.type === 'HOLD_IN') {
    scale = 1.45;
  } else if (breathingPhase.type === 'EXHALE') {
    scale = 1.45 - breathingPhaseProgress * 0.45;
  } else if (breathingPhase.type === 'HOLD_OUT') {
    scale = 1.0;
  }

  return (
    <Modal
      visible={activeOverlay === 'GUIDED_BREATHING'}
      animationType="fade"
      transparent={false}
      onRequestClose={closeOverlay}
    >
      <LinearGradient
        colors={['#0f172a', '#1e293b', '#0f172a']}
        style={styles.container}
      >
        {/* Top Header */}
        <View style={styles.header}>
          <TouchableOpacity
            style={styles.closeBtn}
            onPress={closeOverlay}
            accessibilityLabel="Close Session"
          >
            <Feather name="x" size={24} color="#f8fafc" />
          </TouchableOpacity>
          <View style={styles.headerCenter}>
            <Text style={styles.headerTitle}>Box Breathing (4-4-4-4)</Text>
            <Text style={styles.headerSub}>Cycle #{breathingCycleCount} • Remaining {timeFormatted}</Text>
          </View>
          <View style={{ width: 40 }} />
        </View>

        {/* Center Animated Breathing Visualizer */}
        <View style={styles.centerSection}>
          <View
            style={[
              styles.outerBreathingGlow,
              {
                transform: [{ scale }],
              },
            ]}
          >
            <View style={styles.innerBreathingCircle}>
              <Ionicons name="leaf" size={32} color="#10b981" />
              <Text style={styles.phaseLabel}>{breathingPhase.type}</Text>
            </View>
          </View>

          <Text style={styles.phasePromptText}>{breathingPhase.label}</Text>
          <Text style={styles.phaseTimerText}>
            {Math.ceil(breathingPhase.seconds * (1 - breathingPhaseProgress))}s
          </Text>
        </View>

        {/* Live Vitals Biofeedback Footer */}
        <View style={styles.footerVitals}>
          <View style={styles.vitalCard}>
            <Text style={styles.vitalLabel}>CURRENT HR</Text>
            <Text style={styles.vitalValue}>{vitals.heartRate} bpm</Text>
          </View>
          <View style={styles.vitalCard}>
            <Text style={styles.vitalLabel}>HRV RMSSD</Text>
            <Text style={[styles.vitalValue, { color: '#10b981' }]}>{vitals.hrvRmssd} ms</Text>
          </View>
          <View style={styles.vitalCard}>
            <Text style={styles.vitalLabel}>SKIN GSR</Text>
            <Text style={styles.vitalValue}>{vitals.gsrMicrosiemens} μS</Text>
          </View>
        </View>

        {/* Finish Session Early Button */}
        <TouchableOpacity style={styles.endEarlyButton} onPress={closeOverlay}>
          <Text style={styles.endEarlyText}>End Breathing Session</Text>
        </TouchableOpacity>
      </LinearGradient>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 50,
    paddingHorizontal: 20,
    paddingBottom: 40,
    justifyContent: 'space-between',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  closeBtn: {
    width: 40,
    height: 40,
    borderRadius: 20,
    backgroundColor: 'rgba(255,255,255,0.1)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  headerCenter: {
    alignItems: 'center',
  },
  headerTitle: {
    color: '#ffffff',
    fontSize: 18,
    fontWeight: '700',
  },
  headerSub: {
    color: '#94a3b8',
    fontSize: 12,
    marginTop: 2,
  },
  centerSection: {
    alignItems: 'center',
    justifyContent: 'center',
    marginVertical: 40,
  },
  outerBreathingGlow: {
    width: 220,
    height: 220,
    borderRadius: 110,
    backgroundColor: 'rgba(16, 185, 129, 0.15)',
    borderWidth: 2,
    borderColor: 'rgba(16, 185, 129, 0.4)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  innerBreathingCircle: {
    width: 140,
    height: 140,
    borderRadius: 70,
    backgroundColor: 'rgba(16, 185, 129, 0.3)',
    borderWidth: 2,
    borderColor: '#10b981',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 6,
  },
  phaseLabel: {
    color: '#ffffff',
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 1,
  },
  phasePromptText: {
    color: '#e2e8f0',
    fontSize: 18,
    fontWeight: '600',
    marginTop: 36,
    textAlign: 'center',
  },
  phaseTimerText: {
    color: '#10b981',
    fontSize: 32,
    fontWeight: '800',
    marginTop: 8,
  },
  footerVitals: {
    flexDirection: 'row',
    gap: 12,
    backgroundColor: 'rgba(255,255,255,0.05)',
    padding: 14,
    borderRadius: 18,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  vitalCard: {
    flex: 1,
    alignItems: 'center',
  },
  vitalLabel: {
    color: '#94a3b8',
    fontSize: 10,
    fontWeight: '700',
    letterSpacing: 0.5,
  },
  vitalValue: {
    color: '#ffffff',
    fontSize: 15,
    fontWeight: '700',
    marginTop: 4,
  },
  endEarlyButton: {
    paddingVertical: 14,
    borderRadius: 14,
    backgroundColor: 'rgba(239, 68, 68, 0.2)',
    borderWidth: 1,
    borderColor: 'rgba(239, 68, 68, 0.4)',
    alignItems: 'center',
  },
  endEarlyText: {
    color: '#f87171',
    fontSize: 14,
    fontWeight: '700',
  },
});
