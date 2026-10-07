import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Ionicons, Feather, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';
import { CircularGauge } from '../components/CircularGauge';
import { SparklineChart } from '../components/SparklineChart';

export const DashboardScreen: React.FC = () => {
  const { vitals, isDarkMode, openOverlay, setCurrentTab } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      showsVerticalScrollIndicator={false}
    >
      {/* Hero Stress Gauge Card */}
      <View
        style={[
          styles.card,
          {
            backgroundColor: colors.cardBackground,
            borderColor: colors.cardBorder,
          },
        ]}
      >
        <View style={styles.cardHeader}>
          <View>
            <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>
              Current Stress State
            </Text>
            <Text style={[styles.cardSubtitle, { color: colors.textSecondary }]}>
              Continuous Wearable Bio-Feedback
            </Text>
          </View>
          <TouchableOpacity
            style={[styles.actionTag, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
            onPress={() => openOverlay('CALIBRATION')}
          >
            <Feather name="sliders" size={13} color={colors.primary} />
            <Text style={[styles.actionTagText, { color: colors.primary }]}>Calibrate</Text>
          </TouchableOpacity>
        </View>

        <CircularGauge score={vitals.stressScore} size={180} />

        <View style={styles.gaugeFooter}>
          <Text style={[styles.footerStatus, { color: colors.textSecondary }]}>
            Physiological Load:{' '}
            <Text style={{ fontWeight: '700', color: colors.textPrimary }}>
              {vitals.stressCategory === 'HIGH'
                ? 'Elevated Sympathetic Tone'
                : vitals.stressCategory === 'ELEVATED'
                ? 'Mild Stress Reaction'
                : 'Balanced Parasympathetic State'}
            </Text>
          </Text>
        </View>
      </View>

      {/* Quick Interventions Launchers */}
      <View style={styles.actionsRow}>
        <TouchableOpacity
          style={[styles.quickActionButton, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}
          onPress={() => openOverlay('GUIDED_BREATHING')}
          activeOpacity={0.8}
        >
          <LinearGradient
            colors={['#10b981', '#059669']}
            style={styles.actionIconContainer}
          >
            <Ionicons name="leaf-outline" size={20} color="#ffffff" />
          </LinearGradient>
          <Text style={[styles.quickActionTitle, { color: colors.textPrimary }]}>Box Breathing</Text>
          <Text style={[styles.quickActionSub, { color: colors.textMuted }]}>4-4-4-4 Cycle</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.quickActionButton, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}
          onPress={() => openOverlay('AUDIO_PLAYER')}
          activeOpacity={0.8}
        >
          <LinearGradient
            colors={['#3b82f6', '#1d4ed8']}
            style={styles.actionIconContainer}
          >
            <Ionicons name="headset-outline" size={20} color="#ffffff" />
          </LinearGradient>
          <Text style={[styles.quickActionTitle, { color: colors.textPrimary }]}>Binaural Beats</Text>
          <Text style={[styles.quickActionSub, { color: colors.textMuted }]}>432 Hz Sound</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.quickActionButton, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}
          onPress={() => openOverlay('MIGRAINE_DETAIL')}
          activeOpacity={0.8}
        >
          <LinearGradient
            colors={['#8b5cf6', '#6d28d9']}
            style={styles.actionIconContainer}
          >
            <Ionicons name="thunderstorm-outline" size={20} color="#ffffff" />
          </LinearGradient>
          <Text style={[styles.quickActionTitle, { color: colors.textPrimary }]}>Migraine Risk</Text>
          <Text style={[styles.quickActionSub, { color: colors.textMuted }]}>AI Forecast</Text>
        </TouchableOpacity>
      </View>

      {/* Primary Biometric Metrics Grid */}
      <Text style={[styles.sectionHeading, { color: colors.textPrimary }]}>Live Biometrics</Text>

      <View style={styles.metricsGrid}>
        {/* Heart Rate */}
        <View style={[styles.metricCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.metricCardTop}>
            <Text style={[styles.metricLabel, { color: colors.textSecondary }]}>Heart Rate</Text>
            <Ionicons name="heart" size={18} color="#ef4444" />
          </View>
          <Text style={[styles.metricValue, { color: colors.textPrimary }]}>
            {vitals.heartRate} <Text style={styles.metricUnit}>BPM</Text>
          </Text>
          <SparklineChart data={[68, 70, 72, 75, 71, 74, vitals.heartRate]} color="#ef4444" height={36} />
        </View>

        {/* HRV RMSSD */}
        <View style={[styles.metricCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.metricCardTop}>
            <Text style={[styles.metricLabel, { color: colors.textSecondary }]}>HRV (RMSSD)</Text>
            <Ionicons name="pulse" size={18} color="#10b981" />
          </View>
          <Text style={[styles.metricValue, { color: colors.textPrimary }]}>
            {vitals.hrvRmssd} <Text style={styles.metricUnit}>ms</Text>
          </Text>
          <SparklineChart data={[55, 58, 60, 56, 59, vitals.hrvRmssd]} color="#10b981" height={36} />
        </View>

        {/* Electrodermal GSR */}
        <View style={[styles.metricCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.metricCardTop}>
            <Text style={[styles.metricLabel, { color: colors.textSecondary }]}>GSR (Skin Conduct.)</Text>
            <Ionicons name="water-outline" size={18} color="#0284c7" />
          </View>
          <Text style={[styles.metricValue, { color: colors.textPrimary }]}>
            {vitals.gsrMicrosiemens} <Text style={styles.metricUnit}>μS</Text>
          </Text>
          <SparklineChart data={[2.1, 2.3, 2.2, 2.5, 2.4, vitals.gsrMicrosiemens]} color="#0284c7" height={36} />
        </View>

        {/* Skin Temperature */}
        <View style={[styles.metricCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.metricCardTop}>
            <Text style={[styles.metricLabel, { color: colors.textSecondary }]}>Skin Temp</Text>
            <MaterialCommunityIcons name="thermometer" size={18} color="#f59e0b" />
          </View>
          <Text style={[styles.metricValue, { color: colors.textPrimary }]}>
            {vitals.skinTempCelsius}° <Text style={styles.metricUnit}>C</Text>
          </Text>
          <SparklineChart data={[34.0, 34.1, 34.1, 34.2, vitals.skinTempCelsius]} color="#f59e0b" height={36} />
        </View>
      </View>

      {/* Wearable OLED Screen Mirror */}
      <View
        style={[
          styles.oledCard,
          {
            backgroundColor: isDarkMode ? '#0a0f1d' : '#f0f7ff',
            borderColor: isDarkMode ? '#1e293b' : '#c8dcf5',
          },
        ]}
      >
        <View style={styles.oledHeader}>
          <View style={styles.oledHeaderLeft}>
            <MaterialCommunityIcons name="watch" size={16} color={colors.primary} />
            <Text style={[styles.oledLabel, { color: colors.textSecondary }]}>
              Wearable OLED Display Mirror
            </Text>
          </View>
          <Text style={[styles.oledLiveTag, { color: colors.accent }]}>● LIVE</Text>
        </View>

        <View style={styles.oledScreen}>
          <Text style={styles.oledTextHeader}>STRESSCARE BAND</Text>
          <View style={styles.oledRow}>
            <Text style={styles.oledMetricText}>
              HR: <Text style={styles.oledBrightText}>{vitals.heartRate} bpm</Text>
            </Text>
            <Text style={styles.oledMetricText}>
              STRESS: <Text style={styles.oledBrightText}>{vitals.stressScore}%</Text>
            </Text>
          </View>
          <View style={styles.oledRow}>
            <Text style={styles.oledMetricText}>
              GSR: <Text style={styles.oledBrightText}>{vitals.gsrMicrosiemens} μS</Text>
            </Text>
            <Text style={styles.oledMetricText}>
              BATT: <Text style={styles.oledBrightText}>{vitals.batteryPercent}%</Text>
            </Text>
          </View>
        </View>
      </View>

      {/* PSS Assessment Prompt */}
      <TouchableOpacity
        style={[
          styles.pssBanner,
          {
            backgroundColor: colors.cardBackground,
            borderColor: colors.cardBorder,
          },
        ]}
        onPress={() => openOverlay('PSS_QUESTIONNAIRE')}
        activeOpacity={0.8}
      >
        <View style={[styles.pssIconCircle, { backgroundColor: colors.surfaceSoft }]}>
          <Feather name="clipboard" size={22} color={colors.primary} />
        </View>
        <View style={styles.pssContent}>
          <Text style={[styles.pssTitle, { color: colors.textPrimary }]}>
            Perceived Stress Scale (PSS-10)
          </Text>
          <Text style={[styles.pssSub, { color: colors.textSecondary }]}>
            Standardized clinical questionnaire to calibrate subjective stress.
          </Text>
        </View>
        <Feather name="chevron-right" size={20} color={colors.textMuted} />
      </TouchableOpacity>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  contentContainer: {
    padding: 16,
    paddingBottom: 24,
    gap: 16,
  },
  card: {
    borderRadius: 20,
    padding: 20,
    borderWidth: 1,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.05,
    shadowRadius: 8,
    elevation: 2,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: '700',
  },
  cardSubtitle: {
    fontSize: 12,
    marginTop: 2,
  },
  actionTag: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 16,
    borderWidth: 1,
  },
  actionTagText: {
    fontSize: 12,
    fontWeight: '600',
  },
  gaugeFooter: {
    alignItems: 'center',
    marginTop: 16,
    paddingTop: 12,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
  },
  footerStatus: {
    fontSize: 13,
  },
  actionsRow: {
    flexDirection: 'row',
    gap: 10,
  },
  quickActionButton: {
    flex: 1,
    borderRadius: 16,
    padding: 14,
    borderWidth: 1,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.03,
    shadowRadius: 4,
    elevation: 1,
  },
  actionIconContainer: {
    width: 40,
    height: 40,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 8,
  },
  quickActionTitle: {
    fontSize: 12,
    fontWeight: '700',
    textAlign: 'center',
  },
  quickActionSub: {
    fontSize: 10,
    marginTop: 2,
    textAlign: 'center',
  },
  sectionHeading: {
    fontSize: 16,
    fontWeight: '700',
    marginTop: 4,
  },
  metricsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 12,
  },
  metricCard: {
    width: '48%',
    borderRadius: 16,
    padding: 14,
    borderWidth: 1,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.04,
    shadowRadius: 4,
    elevation: 1,
  },
  metricCardTop: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  metricLabel: {
    fontSize: 11,
    fontWeight: '600',
  },
  metricValue: {
    fontSize: 20,
    fontWeight: '800',
    marginBottom: 4,
  },
  metricUnit: {
    fontSize: 11,
    fontWeight: '500',
  },
  oledCard: {
    borderRadius: 16,
    padding: 16,
    borderWidth: 1,
  },
  oledHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  oledHeaderLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
  },
  oledLabel: {
    fontSize: 12,
    fontWeight: '600',
  },
  oledLiveTag: {
    fontSize: 10,
    fontWeight: '700',
    letterSpacing: 0.5,
  },
  oledScreen: {
    backgroundColor: '#050a14',
    borderRadius: 10,
    padding: 14,
    borderWidth: 1,
    borderColor: '#1e3a5f',
  },
  oledTextHeader: {
    color: '#38bdf8',
    fontSize: 10,
    fontWeight: '800',
    letterSpacing: 1.5,
    marginBottom: 8,
  },
  oledRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 2,
  },
  oledMetricText: {
    color: '#94a3b8',
    fontSize: 12,
    fontFamily: 'monospace',
  },
  oledBrightText: {
    color: '#f8fafc',
    fontWeight: '700',
  },
  pssBanner: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
    borderRadius: 16,
    borderWidth: 1,
    gap: 12,
  },
  pssIconCircle: {
    width: 44,
    height: 44,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  pssContent: {
    flex: 1,
  },
  pssTitle: {
    fontSize: 14,
    fontWeight: '700',
  },
  pssSub: {
    fontSize: 11,
    marginTop: 2,
  },
});
