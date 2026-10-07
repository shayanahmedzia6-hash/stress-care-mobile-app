import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import { Feather, Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const MigraineRiskOverlay: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <Modal
      visible={activeOverlay === 'MIGRAINE_DETAIL'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View style={styles.headerLeft}>
              <Ionicons name="thunderstorm" size={22} color="#8b5cf6" />
              <Text style={[styles.title, { color: colors.textPrimary }]}>
                Migraine Risk Assessment
              </Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={22} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
            Predictive AI model combining wearable autonomic stress telemetry with local meteorological barometric trends.
          </Text>

          <ScrollView style={styles.scrollArea} showsVerticalScrollIndicator={false}>
            {/* 24-hr Probability Card */}
            <View style={[styles.riskScoreCard, { backgroundColor: colors.surfaceSoft }]}>
              <Text style={[styles.riskScoreLabel, { color: colors.textMuted }]}>
                24-HOUR FORECAST PROBABILITY
              </Text>
              <Text style={[styles.riskScoreVal, { color: '#10b981' }]}>22%</Text>
              <View style={[styles.riskCategoryBadge, { backgroundColor: '#10b98120' }]}>
                <Text style={{ color: '#10b981', fontWeight: '700', fontSize: 12 }}>
                  Low Vulnerability State
                </Text>
              </View>
            </View>

            {/* Contributing Biological Triggers */}
            <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>
              Bio-Environmental Factors
            </Text>

            {/* Factor 1: Barometric Pressure */}
            <View style={[styles.factorCard, { borderColor: colors.cardBorder }]}>
              <View style={styles.factorHeader}>
                <MaterialCommunityIcons name="weather-windy" size={20} color="#0284c7" />
                <Text style={[styles.factorName, { color: colors.textPrimary }]}>
                  Barometric Atmospheric Pressure
                </Text>
                <Text style={[styles.factorStatus, { color: '#10b981' }]}>Stable (1014 hPa)</Text>
              </View>
              <Text style={[styles.factorDesc, { color: colors.textSecondary }]}>
                No rapid atmospheric pressure drops detected in your local region. Low risk for barometric vascular trigeminal headache.
              </Text>
            </View>

            {/* Factor 2: Sleep Deficit */}
            <View style={[styles.factorCard, { borderColor: colors.cardBorder }]}>
              <View style={styles.factorHeader}>
                <Ionicons name="moon-outline" size={20} color="#8b5cf6" />
                <Text style={[styles.factorName, { color: colors.textPrimary }]}>
                  Sleep Debt & Disruption
                </Text>
                <Text style={[styles.factorStatus, { color: '#f59e0b' }]}>Mild (45 min deficit)</Text>
              </View>
              <Text style={[styles.factorDesc, { color: colors.textSecondary }]}>
                REM sleep duration was slightly abbreviated last night, slightly lowering neurovascular threshold.
              </Text>
            </View>

            {/* Factor 3: Sympathetic Arousal */}
            <View style={[styles.factorCard, { borderColor: colors.cardBorder }]}>
              <View style={styles.factorHeader}>
                <Ionicons name="pulse" size={20} color="#ef4444" />
                <Text style={[styles.factorName, { color: colors.textPrimary }]}>
                  Sympathetic Phasic Arousal
                </Text>
                <Text style={[styles.factorStatus, { color: '#10b981' }]}>Low-Moderate (36/100)</Text>
              </View>
              <Text style={[styles.factorDesc, { color: colors.textSecondary }]}>
                Electrodermal tonic levels show balanced autonomic control without sustained vasoconstriction.
              </Text>
            </View>

            {/* Preventative Recommendations */}
            <Text style={[styles.sectionTitle, { color: colors.textPrimary, marginTop: 16 }]}>
              Preventative Guidelines
            </Text>

            <View style={[styles.tipsBox, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
              <Text style={[styles.tipItem, { color: colors.textPrimary }]}>
                💧 <Text style={{ fontWeight: '700' }}>Maintain Hydration:</Text> Drink at least 500 mL water over the next 2 hours.
              </Text>
              <Text style={[styles.tipItem, { color: colors.textPrimary }]}>
                🧘 <Text style={{ fontWeight: '700' }}>Box Breathing:</Text> Perform one 2-minute cycle if screen brightness causes eye strain.
              </Text>
              <Text style={[styles.tipItem, { color: colors.textPrimary }]}>
                🕶️ <Text style={{ fontWeight: '700' }}>Visual Rest:</Text> Take 20-second breaks looking at 20-foot distance every 20 minutes.
              </Text>
            </View>

            <TouchableOpacity
              style={[styles.closeBtn, { backgroundColor: colors.primary }]}
              onPress={closeOverlay}
            >
              <Text style={styles.closeBtnText}>Return to Dashboard</Text>
            </TouchableOpacity>
          </ScrollView>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  modalCard: {
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    padding: 22,
    maxHeight: '85%',
    borderWidth: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  headerLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  title: {
    fontSize: 18,
    fontWeight: '700',
  },
  subtitle: {
    fontSize: 12,
    marginBottom: 16,
    lineHeight: 16,
  },
  scrollArea: {
    maxHeight: 480,
  },
  riskScoreCard: {
    borderRadius: 16,
    padding: 16,
    alignItems: 'center',
    marginBottom: 16,
  },
  riskScoreLabel: {
    fontSize: 10,
    fontWeight: '700',
    letterSpacing: 0.5,
  },
  riskScoreVal: {
    fontSize: 44,
    fontWeight: '800',
    marginVertical: 4,
  },
  riskCategoryBadge: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 10,
  },
  sectionTitle: {
    fontSize: 14,
    fontWeight: '700',
    marginBottom: 10,
  },
  factorCard: {
    borderRadius: 14,
    padding: 14,
    borderWidth: 1,
    marginBottom: 10,
  },
  factorHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginBottom: 6,
  },
  factorName: {
    flex: 1,
    fontSize: 13,
    fontWeight: '700',
  },
  factorStatus: {
    fontSize: 11,
    fontWeight: '700',
  },
  factorDesc: {
    fontSize: 11,
    lineHeight: 16,
  },
  tipsBox: {
    borderRadius: 14,
    padding: 14,
    borderWidth: 1,
    gap: 8,
    marginBottom: 18,
  },
  tipItem: {
    fontSize: 12,
    lineHeight: 17,
  },
  closeBtn: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
    marginBottom: 12,
  },
  closeBtnText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
