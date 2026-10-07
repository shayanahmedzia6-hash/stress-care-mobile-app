import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
} from 'react-native';
import { Feather, Ionicons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const CalibrationModal: React.FC = () => {
  const {
    activeOverlay,
    closeOverlay,
    isDarkMode,
    calibrationProgress,
    calibrationRemainingSeconds,
    calibrationSteps,
    startCalibration,
  } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <Modal
      visible={activeOverlay === 'CALIBRATION'}
      animationType="fade"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View style={styles.headerLeft}>
              <Feather name="sliders" size={20} color={colors.primary} />
              <Text style={[styles.title, { color: colors.textPrimary }]}>Baseline Sensor Calibration</Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={20} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <Text style={[styles.instruction, { color: colors.textSecondary }]}>
            Sit quietly in a comfortable position with your hand resting flat. Avoid moving, speaking, or drinking coffee during this 60-second baseline collection.
          </Text>

          {/* Countdown timer & progress */}
          <View style={[styles.timerBox, { backgroundColor: colors.surfaceSoft }]}>
            <Text style={[styles.timerValue, { color: colors.primary }]}>
              {calibrationRemainingSeconds}s
            </Text>
            <Text style={[styles.timerLabel, { color: colors.textMuted }]}>
              {calibrationRemainingSeconds > 0 ? 'Sampling Biometric Baseline...' : 'Calibration Complete'}
            </Text>

            <View style={styles.progressBarWrapper}>
              <View
                style={[
                  styles.progressBarFill,
                  { width: `${calibrationProgress * 100}%`, backgroundColor: colors.primary },
                ]}
              />
            </View>
          </View>

          {/* Step items */}
          <View style={styles.stepsContainer}>
            {calibrationSteps.map((step, idx) => (
              <View key={idx} style={[styles.stepRow, { borderBottomColor: colors.cardBorder }]}>
                <View
                  style={[
                    styles.stepIndicator,
                    {
                      backgroundColor: step.isCompleted
                        ? '#10b981'
                        : step.isInProgress
                        ? colors.primary
                        : colors.cardBorder,
                    },
                  ]}
                >
                  {step.isCompleted ? (
                    <Ionicons name="checkmark" size={14} color="#ffffff" />
                  ) : (
                    <Text style={styles.stepNum}>{idx + 1}</Text>
                  )}
                </View>

                <View style={{ flex: 1 }}>
                  <Text style={[styles.stepTitle, { color: colors.textPrimary }]}>{step.title}</Text>
                  <Text style={[styles.stepDesc, { color: colors.textSecondary }]}>{step.description}</Text>
                </View>

                <Text
                  style={[
                    styles.stepVal,
                    {
                      color: step.isCompleted
                        ? '#10b981'
                        : step.isInProgress
                        ? colors.primary
                        : colors.textMuted,
                    },
                  ]}
                >
                  {step.value}
                </Text>
              </View>
            ))}
          </View>

          {/* Action Button */}
          <View style={styles.footerRow}>
            {calibrationRemainingSeconds > 0 ? (
              <TouchableOpacity
                style={[styles.cancelBtn, { borderColor: colors.cardBorder }]}
                onPress={closeOverlay}
              >
                <Text style={[styles.cancelBtnText, { color: colors.textSecondary }]}>Cancel</Text>
              </TouchableOpacity>
            ) : (
              <TouchableOpacity
                style={[styles.doneBtn, { backgroundColor: colors.accent }]}
                onPress={closeOverlay}
              >
                <Text style={styles.doneBtnText}>Save & Apply Baseline</Text>
              </TouchableOpacity>
            )}

            <TouchableOpacity
              style={[styles.restartBtn, { backgroundColor: colors.primary }]}
              onPress={startCalibration}
            >
              <Text style={styles.restartBtnText}>Restart 60s</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.6)',
    justifyContent: 'center',
    padding: 20,
  },
  modalCard: {
    borderRadius: 22,
    padding: 22,
    borderWidth: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  headerLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  title: {
    fontSize: 17,
    fontWeight: '700',
  },
  instruction: {
    fontSize: 12,
    lineHeight: 17,
    marginBottom: 16,
  },
  timerBox: {
    padding: 16,
    borderRadius: 16,
    alignItems: 'center',
    marginBottom: 16,
  },
  timerValue: {
    fontSize: 38,
    fontWeight: '800',
  },
  timerLabel: {
    fontSize: 12,
    fontWeight: '500',
    marginTop: 2,
    marginBottom: 12,
  },
  progressBarWrapper: {
    height: 6,
    width: '100%',
    backgroundColor: 'rgba(0,0,0,0.08)',
    borderRadius: 3,
    overflow: 'hidden',
  },
  progressBarFill: {
    height: '100%',
  },
  stepsContainer: {
    gap: 12,
    marginBottom: 18,
  },
  stepRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingBottom: 10,
    borderBottomWidth: 1,
    gap: 12,
  },
  stepIndicator: {
    width: 26,
    height: 26,
    borderRadius: 13,
    alignItems: 'center',
    justifyContent: 'center',
  },
  stepNum: {
    color: '#ffffff',
    fontSize: 11,
    fontWeight: '700',
  },
  stepTitle: {
    fontSize: 13,
    fontWeight: '600',
  },
  stepDesc: {
    fontSize: 11,
    marginTop: 1,
  },
  stepVal: {
    fontSize: 11,
    fontWeight: '700',
  },
  footerRow: {
    flexDirection: 'row',
    gap: 10,
  },
  cancelBtn: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 12,
    borderWidth: 1,
    alignItems: 'center',
  },
  cancelBtnText: {
    fontSize: 13,
    fontWeight: '600',
  },
  doneBtn: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 12,
    alignItems: 'center',
  },
  doneBtnText: {
    color: '#ffffff',
    fontSize: 13,
    fontWeight: '700',
  },
  restartBtn: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 12,
    alignItems: 'center',
  },
  restartBtnText: {
    color: '#ffffff',
    fontSize: 13,
    fontWeight: '700',
  },
});
