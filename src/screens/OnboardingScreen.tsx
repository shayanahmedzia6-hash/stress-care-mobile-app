import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Feather, Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

const { width } = Dimensions.get('window');

const steps = [
  {
    title: 'Track Your Stress\nin Real Time',
    description:
      'Monitor heart rate, GSR, skin temperature\nand motion using your wearable device.',
    metrics: [
      { icon: 'heart', label: 'HR', value: '74', color: '#ef4444', bg: '#fee2e2' },
      { icon: 'water', label: 'GSR', value: '2.4', color: '#3b82f6', bg: '#dbeafe' },
      { icon: 'thermometer', label: 'Temp', value: '34°', color: '#f59e0b', bg: '#fef3c7' },
      { icon: 'activity', label: 'Motion', value: '0.9', color: '#10b981', bg: '#d1fae5' },
    ],
  },
  {
    title: 'AI-Powered\nInterventions',
    description:
      'Get guided box breathing, binaural soundscapes\nand clinical PSS assessments when stress rises.',
    metrics: [
      { icon: 'wind', label: 'Breathe', value: '4-4', color: '#1d68bd', bg: '#eaf2ff' },
      { icon: 'musical-notes', label: 'Audio', value: '432', color: '#8b5cf6', bg: '#ede9fe' },
      { icon: 'clipboard', label: 'PSS-10', value: 'OK', color: '#10b981', bg: '#d1fae5' },
      { icon: 'pulse', label: 'HRV', value: '58', color: '#ef4444', bg: '#fee2e2' },
    ],
  },
  {
    title: 'Sleep & Migraine\nInsights',
    description:
      'Forecast migraine risk and track recovery\nwith wearable telemetry and sleep trends.',
    metrics: [
      { icon: 'moon', label: 'Sleep', value: '7.2h', color: '#6366f1', bg: '#e0e7ff' },
      { icon: 'flash', label: 'Risk', value: 'Low', color: '#10b981', bg: '#d1fae5' },
      { icon: 'cloud', label: 'Sync', value: 'On', color: '#1d68bd', bg: '#eaf2ff' },
      { icon: 'shield-checkmark', label: 'Safe', value: 'HIPAA', color: '#0ea5e9', bg: '#e0f2fe' },
    ],
  },
];

export const OnboardingScreen: React.FC = () => {
  const { isDarkMode, completeOnboarding } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;
  const [currentStep, setCurrentStep] = useState(0);
  const step = steps[currentStep];

  const handleNext = () => {
    if (currentStep < steps.length - 1) {
      setCurrentStep((s) => s + 1);
    } else {
      completeOnboarding();
    }
  };

  const gradient = isDarkMode
    ? (['#0F172A', '#0B0F19', '#070B12'] as const)
    : (['#EAF3FF', '#F5FAFF', '#FFFFFF'] as const);

  return (
    <LinearGradient colors={gradient} style={styles.container}>
      {/* Header: logo + Skip */}
      <View style={styles.header}>
        <View style={styles.logoRow}>
          <View style={[styles.logoMark, { backgroundColor: colors.primary }]}>
            <Ionicons name="heart" size={16} color="#fff" />
          </View>
          <Text style={[styles.logoText, { color: colors.textPrimary }]}>StressCare</Text>
        </View>
        {currentStep < steps.length - 1 ? (
          <TouchableOpacity onPress={completeOnboarding} hitSlop={12}>
            <Text style={[styles.skipText, { color: colors.primary }]}>Skip</Text>
          </TouchableOpacity>
        ) : (
          <View style={{ width: 48 }} />
        )}
      </View>

      {/* Content */}
      <View style={styles.content}>
        <Text style={[styles.title, { color: colors.textPrimary }]}>{step.title}</Text>
        <Text style={[styles.description, { color: colors.textSecondary }]}>{step.description}</Text>

        <View style={[styles.heroCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={[styles.heroCircle, { backgroundColor: colors.surfaceSoft }]}>
            {currentStep === 0 && <MaterialCommunityIcons name="watch" size={48} color={colors.primary} />}
            {currentStep === 1 && <Ionicons name="leaf" size={48} color={colors.accent} />}
            {currentStep === 2 && <Ionicons name="shield-checkmark" size={48} color="#8b5cf6" />}
          </View>
          <View style={styles.metricsGrid}>
            {step.metrics.map((m) => (
              <View key={m.label} style={[styles.metricChip, { backgroundColor: m.bg }]}>
                <Ionicons name={m.icon as any} size={14} color={m.color} />
                <Text style={[styles.metricLabel, { color: m.color }]}>{m.label}</Text>
                <Text style={[styles.metricValue, { color: colors.textPrimary }]}>{m.value}</Text>
              </View>
            ))}
          </View>
        </View>
      </View>

      {/* Footer: dots + Next */}
      <View style={styles.footer}>
        <View style={styles.dotsRow}>
          {steps.map((_, idx) => (
            <View
              key={idx}
              style={[
                styles.dot,
                {
                  width: idx === currentStep ? 24 : 9,
                  backgroundColor:
                    idx === currentStep
                      ? colors.primary
                      : isDarkMode
                        ? '#334155'
                        : '#CCD9EE',
                },
              ]}
            />
          ))}
        </View>

        <TouchableOpacity
          style={[styles.nextBtn, { backgroundColor: colors.primary }]}
          onPress={handleNext}
          activeOpacity={0.85}
        >
          <Text style={styles.nextBtnText}>
            {currentStep === steps.length - 1 ? 'Get Started' : 'Next'}
          </Text>
          <Feather name="chevron-right" size={18} color="#fff" />
        </TouchableOpacity>
      </View>
    </LinearGradient>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 20,
    paddingTop: 16,
    paddingBottom: 24,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 8,
  },
  logoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  logoMark: {
    width: 32,
    height: 32,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  logoText: {
    fontSize: 18,
    fontWeight: '800',
  },
  skipText: {
    fontSize: 14,
    fontWeight: '600',
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 28,
    fontWeight: '800',
    textAlign: 'center',
    lineHeight: 34,
    marginBottom: 10,
  },
  description: {
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 20,
    marginBottom: 28,
  },
  heroCard: {
    width: width - 48,
    borderRadius: 24,
    borderWidth: 1,
    padding: 20,
    alignItems: 'center',
  },
  heroCircle: {
    width: 96,
    height: 96,
    borderRadius: 48,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 18,
  },
  metricsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'center',
    gap: 10,
  },
  metricChip: {
    width: (width - 100) / 2,
    borderRadius: 14,
    paddingVertical: 10,
    paddingHorizontal: 12,
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  metricLabel: {
    fontSize: 11,
    fontWeight: '700',
    flex: 1,
  },
  metricValue: {
    fontSize: 12,
    fontWeight: '800',
  },
  footer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingBottom: 8,
  },
  dotsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  dot: {
    height: 9,
    borderRadius: 5,
  },
  nextBtn: {
    height: 48,
    paddingHorizontal: 22,
    borderRadius: 50,
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    elevation: 4,
    shadowColor: '#1d68bd',
    shadowOpacity: 0.25,
    shadowRadius: 8,
    shadowOffset: { width: 0, height: 4 },
  },
  nextBtnText: {
    color: '#fff',
    fontSize: 15,
    fontWeight: '700',
  },
});
