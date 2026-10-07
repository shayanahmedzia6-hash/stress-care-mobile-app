import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
} from 'react-native';
import { Feather, Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

const steps = [
  {
    icon: 'watch',
    title: 'Wearable Biosensors',
    description:
      'Continuous 50Hz optical PPG heart rate, autonomic galvanic skin response (GSR), and peripheral thermistor telemetry.',
  },
  {
    icon: 'pulse',
    title: 'Real-Time Biofeedback',
    description:
      'Instant AI calculation of sympathetic autonomic nervous system arousal vs parasympathetic recovery tone.',
  },
  {
    icon: 'leaf',
    title: 'Active Clinical Interventions',
    description:
      'Instant vagus nerve down-regulation via 4-4-4-4 box breathing, binaural soundscapes (432Hz/528Hz), and grounding.',
  },
  {
    icon: 'shield-check',
    title: 'Migraine & Sleep Analytics',
    description:
      'Predictive 24-hour migraine triggers correlated with local barometric pressure shifts and nocturnal sleep deficits.',
  },
];

export const OnboardingOverlay: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [currentStep, setCurrentStep] = useState(0);

  const handleNext = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep(currentStep + 1);
    } else {
      closeOverlay();
    }
  };

  const stepData = steps[currentStep];

  return (
    <Modal
      visible={activeOverlay === 'ONBOARDING'}
      animationType="fade"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <Text style={[styles.stepCount, { color: colors.textMuted }]}>
              STEP {currentStep + 1} OF {steps.length}
            </Text>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={20} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <View style={styles.centerHero}>
            <View style={[styles.iconCircle, { backgroundColor: colors.surfaceSoft }]}>
              {currentStep === 0 && <MaterialCommunityIcons name="watch" size={40} color={colors.primary} />}
              {currentStep === 1 && <Ionicons name="pulse" size={40} color={colors.primary} />}
              {currentStep === 2 && <Ionicons name="leaf" size={40} color={colors.accent} />}
              {currentStep === 3 && <MaterialCommunityIcons name="shield-check" size={40} color="#8b5cf6" />}
            </View>

            <Text style={[styles.stepTitle, { color: colors.textPrimary }]}>{stepData.title}</Text>
            <Text style={[styles.stepDesc, { color: colors.textSecondary }]}>
              {stepData.description}
            </Text>
          </View>

          {/* Dots Indicator */}
          <View style={styles.dotsRow}>
            {steps.map((_, idx) => (
              <View
                key={idx}
                style={[
                  styles.dot,
                  {
                    backgroundColor: idx === currentStep ? colors.primary : colors.cardBorder,
                    width: idx === currentStep ? 20 : 8,
                  },
                ]}
              />
            ))}
          </View>

          <TouchableOpacity
            style={[styles.nextBtn, { backgroundColor: colors.primary }]}
            onPress={handleNext}
          >
            <Text style={styles.nextBtnText}>
              {currentStep === steps.length - 1 ? 'Get Started' : 'Next'}
            </Text>
          </TouchableOpacity>
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
    padding: 24,
  },
  modalCard: {
    borderRadius: 24,
    padding: 24,
    borderWidth: 1,
    alignItems: 'center',
  },
  header: {
    width: '100%',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 20,
  },
  stepCount: {
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 1,
  },
  centerHero: {
    alignItems: 'center',
    paddingHorizontal: 12,
  },
  iconCircle: {
    width: 84,
    height: 84,
    borderRadius: 42,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 20,
  },
  stepTitle: {
    fontSize: 20,
    fontWeight: '700',
    textAlign: 'center',
    marginBottom: 10,
  },
  stepDesc: {
    fontSize: 13,
    textAlign: 'center',
    lineHeight: 19,
  },
  dotsRow: {
    flexDirection: 'row',
    gap: 6,
    marginVertical: 24,
  },
  dot: {
    height: 8,
    borderRadius: 4,
  },
  nextBtn: {
    width: '100%',
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
  },
  nextBtnText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
