import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
} from 'react-native';
import { Feather, Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';
import { SparklineChart } from '../components/SparklineChart';

type TimeRange = 'Day' | 'Week' | 'Month';

export const AnalyticsScreen: React.FC = () => {
  const { isDarkMode, pssResults, openOverlay } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [timeRange, setTimeRange] = useState<TimeRange>('Week');

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      showsVerticalScrollIndicator={false}
    >
      {/* Time Range Filter Bar */}
      <View style={[styles.filterBar, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        {(['Day', 'Week', 'Month'] as TimeRange[]).map((range) => {
          const isSelected = timeRange === range;
          return (
            <TouchableOpacity
              key={range}
              style={[
                styles.filterTab,
                isSelected && { backgroundColor: colors.primary },
              ]}
              onPress={() => setTimeRange(range)}
            >
              <Text
                style={[
                  styles.filterTabText,
                  { color: isSelected ? '#ffffff' : colors.textSecondary },
                ]}
              >
                {range}
              </Text>
            </TouchableOpacity>
          );
        })}
      </View>

      {/* Summary Stat Card */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.cardHeader}>
          <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>Average Stress Level</Text>
          <View style={[styles.trendBadge, { backgroundColor: '#10b98120' }]}>
            <Feather name="trending-down" size={13} color="#10b981" />
            <Text style={styles.trendBadgeText}>-14% vs last week</Text>
          </View>
        </View>

        <View style={styles.scoreRow}>
          <Text style={[styles.bigScore, { color: colors.textPrimary }]}>36</Text>
          <Text style={[styles.bigScale, { color: colors.textMuted }]}>/ 100</Text>
          <View style={styles.scoreCategoryTag}>
            <Text style={{ color: colors.accent, fontWeight: '700', fontSize: 13 }}>Low-Moderate</Text>
          </View>
        </View>

        <Text style={[styles.summaryText, { color: colors.textSecondary }]}>
          Your sympathetic stress activation remained predominantly in the relaxed parasympathetic zone.
        </Text>

        {/* Breakdown distribution bar */}
        <View style={styles.distributionContainer}>
          <Text style={[styles.distLabel, { color: colors.textMuted }]}>Time Distribution</Text>
          <View style={styles.barWrapper}>
            <View style={[styles.barSegment, { flex: 0.65, backgroundColor: '#10b981' }]} />
            <View style={[styles.barSegment, { flex: 0.25, backgroundColor: '#f59e0b' }]} />
            <View style={[styles.barSegment, { flex: 0.1, backgroundColor: '#ef4444' }]} />
          </View>
          <View style={styles.legendRow}>
            <Text style={[styles.legendItem, { color: colors.textSecondary }]}>● Normal (65%)</Text>
            <Text style={[styles.legendItem, { color: colors.textSecondary }]}>● Elevated (25%)</Text>
            <Text style={[styles.legendItem, { color: colors.textSecondary }]}>● High (10%)</Text>
          </View>
        </View>
      </View>

      {/* Circadian Diurnal Stress Trend */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>Hourly Stress Trend</Text>
        <Text style={[styles.cardSubtitle, { color: colors.textSecondary }]}>
          Phasic sympathetic peaks observed around 11:30 AM and 3:45 PM
        </Text>
        <SparklineChart
          data={[24, 22, 20, 28, 42, 65, 48, 38, 56, 42, 30, 26]}
          color={colors.primary}
          height={90}
        />
        <View style={styles.timeAxis}>
          <Text style={[styles.axisText, { color: colors.textMuted }]}>6 AM</Text>
          <Text style={[styles.axisText, { color: colors.textMuted }]}>10 AM</Text>
          <Text style={[styles.axisText, { color: colors.textMuted }]}>2 PM</Text>
          <Text style={[styles.axisText, { color: colors.textMuted }]}>6 PM</Text>
          <Text style={[styles.axisText, { color: colors.textMuted }]}>10 PM</Text>
        </View>
      </View>

      {/* Sleep & Nocturnal Recovery */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.cardHeader}>
          <View>
            <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>Sleep & Autonomic Recovery</Text>
            <Text style={[styles.cardSubtitle, { color: colors.textSecondary }]}>
              Overnight parasympathetic restoration index
            </Text>
          </View>
          <Ionicons name="moon" size={20} color="#8b5cf6" />
        </View>

        <View style={styles.sleepMetricsRow}>
          <View style={[styles.sleepBox, { backgroundColor: colors.surfaceSoft }]}>
            <Text style={[styles.sleepVal, { color: colors.textPrimary }]}>7h 42m</Text>
            <Text style={[styles.sleepLbl, { color: colors.textMuted }]}>Total Duration</Text>
          </View>
          <View style={[styles.sleepBox, { backgroundColor: colors.surfaceSoft }]}>
            <Text style={[styles.sleepVal, { color: '#10b981' }]}>86%</Text>
            <Text style={[styles.sleepLbl, { color: colors.textMuted }]}>Sleep Efficiency</Text>
          </View>
          <View style={[styles.sleepBox, { backgroundColor: colors.surfaceSoft }]}>
            <Text style={[styles.sleepVal, { color: colors.primary }]}>62 ms</Text>
            <Text style={[styles.sleepLbl, { color: colors.textMuted }]}>Nocturnal HRV</Text>
          </View>
        </View>
      </View>

      {/* Migraine Correlation Card */}
      <TouchableOpacity
        style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}
        onPress={() => openOverlay('MIGRAINE_DETAIL')}
        activeOpacity={0.8}
      >
        <View style={styles.cardHeader}>
          <View>
            <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>Migraine Risk Correlation</Text>
            <Text style={[styles.cardSubtitle, { color: colors.textSecondary }]}>
              Multi-factorial bio-trigger analysis
            </Text>
          </View>
          <Feather name="alert-triangle" size={18} color="#f59e0b" />
        </View>

        <Text style={[styles.summaryText, { color: colors.textSecondary }]}>
          Barometric pressure stability and normalized sleep quality have kept your 24-hour migraine risk low (22%).
        </Text>
        <View style={styles.cardActionRow}>
          <Text style={[styles.linkText, { color: colors.primary }]}>View detailed trigger model</Text>
          <Feather name="chevron-right" size={14} color={colors.primary} />
        </View>
      </TouchableOpacity>

      {/* PSS-10 Clinical Questionnaire Logs */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.cardHeader}>
          <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>PSS-10 Assessment Logs</Text>
          <TouchableOpacity onPress={() => openOverlay('PSS_QUESTIONNAIRE')}>
            <Text style={[styles.linkText, { color: colors.primary }]}>+ New Test</Text>
          </TouchableOpacity>
        </View>

        {pssResults.map((item, idx) => (
          <View
            key={idx}
            style={[
              styles.logItem,
              { borderTopColor: colors.cardBorder, borderTopWidth: idx > 0 ? 1 : 0 },
            ]}
          >
            <View>
              <Text style={[styles.logTitle, { color: colors.textPrimary }]}>
                Clinical Score: {item.score} / 40
              </Text>
              <Text style={[styles.logDate, { color: colors.textMuted }]}>
                {new Date(item.timestamp).toLocaleDateString()} • Mood: {item.mood || 'Normal'}
              </Text>
            </View>
            <View
              style={[
                styles.categoryBadge,
                {
                  backgroundColor:
                    item.category === 'HIGH'
                      ? '#ef444420'
                      : item.category === 'MODERATE'
                      ? '#f59e0b20'
                      : '#10b98120',
                },
              ]}
            >
              <Text
                style={{
                  fontSize: 11,
                  fontWeight: '700',
                  color:
                    item.category === 'HIGH'
                      ? '#ef4444'
                      : item.category === 'MODERATE'
                      ? '#f59e0b'
                      : '#10b981',
                }}
              >
                {item.category}
              </Text>
            </View>
          </View>
        ))}
      </View>
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
  filterBar: {
    flexDirection: 'row',
    borderRadius: 14,
    padding: 4,
    borderWidth: 1,
  },
  filterTab: {
    flex: 1,
    paddingVertical: 8,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  filterTabText: {
    fontSize: 13,
    fontWeight: '600',
  },
  card: {
    borderRadius: 18,
    padding: 18,
    borderWidth: 1,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: '700',
  },
  cardSubtitle: {
    fontSize: 11,
    marginTop: 2,
    marginBottom: 10,
  },
  trendBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
  },
  trendBadgeText: {
    fontSize: 11,
    fontWeight: '700',
    color: '#10b981',
  },
  scoreRow: {
    flexDirection: 'row',
    alignItems: 'baseline',
    gap: 6,
    marginVertical: 6,
  },
  bigScore: {
    fontSize: 36,
    fontWeight: '800',
  },
  bigScale: {
    fontSize: 14,
    fontWeight: '600',
  },
  scoreCategoryTag: {
    marginLeft: 8,
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 8,
    backgroundColor: 'rgba(16, 185, 129, 0.1)',
  },
  summaryText: {
    fontSize: 13,
    lineHeight: 18,
    marginTop: 4,
  },
  distributionContainer: {
    marginTop: 16,
    paddingTop: 12,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
  },
  distLabel: {
    fontSize: 11,
    fontWeight: '600',
    marginBottom: 8,
  },
  barWrapper: {
    flexDirection: 'row',
    height: 10,
    borderRadius: 5,
    overflow: 'hidden',
  },
  barSegment: {
    height: '100%',
  },
  legendRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 8,
  },
  legendItem: {
    fontSize: 11,
    fontWeight: '500',
  },
  timeAxis: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 4,
    marginTop: 6,
  },
  axisText: {
    fontSize: 10,
    fontWeight: '500',
  },
  sleepMetricsRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 10,
  },
  sleepBox: {
    flex: 1,
    padding: 12,
    borderRadius: 12,
    alignItems: 'center',
  },
  sleepVal: {
    fontSize: 15,
    fontWeight: '700',
  },
  sleepLbl: {
    fontSize: 10,
    marginTop: 2,
  },
  cardActionRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    marginTop: 10,
  },
  linkText: {
    fontSize: 12,
    fontWeight: '700',
  },
  logItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 12,
  },
  logTitle: {
    fontSize: 13,
    fontWeight: '600',
  },
  logDate: {
    fontSize: 11,
    marginTop: 2,
  },
  categoryBadge: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
});
