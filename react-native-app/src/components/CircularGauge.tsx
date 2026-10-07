import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import Svg, { Circle, Defs, LinearGradient, Stop } from 'react-native-svg';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

interface CircularGaugeProps {
  score: number;
  size?: number;
  strokeWidth?: number;
}

export const CircularGauge: React.FC<CircularGaugeProps> = ({
  score,
  size = 180,
  strokeWidth = 14,
}) => {
  const { isDarkMode, vitals } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  // Semi-circle or 270-degree arc: we use 270 deg (0.75 of circle)
  const arcPercentage = 0.75;
  const totalArcLength = circumference * arcPercentage;
  const clampedScore = Math.min(100, Math.max(0, score));
  const progressLength = (clampedScore / 100) * totalArcLength;
  const strokeDashoffset = totalArcLength - progressLength;

  // Determine stress color
  const statusColor =
    score > 65 ? colors.danger : score > 40 ? colors.warning : colors.accent;

  const statusLabel =
    score > 65 ? 'High Stress' : score > 40 ? 'Moderate Stress' : 'Relaxed / Normal';

  return (
    <View style={[styles.container, { width: size, height: size }]}>
      <Svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <Defs>
          <LinearGradient id="gaugeGradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <Stop offset="0%" stopColor="#10b981" />
            <Stop offset="50%" stopColor="#f59e0b" />
            <Stop offset="100%" stopColor="#ef4444" />
          </LinearGradient>
        </Defs>

        {/* Background Track */}
        <Circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke={colors.gaugeTrack}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={`${totalArcLength} ${circumference}`}
          fill="none"
          transform={`rotate(135 ${size / 2} ${size / 2})`}
        />

        {/* Active Progress */}
        <Circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          stroke={statusColor}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={`${totalArcLength} ${circumference}`}
          strokeDashoffset={strokeDashoffset}
          fill="none"
          transform={`rotate(135 ${size / 2} ${size / 2})`}
        />
      </Svg>

      <View style={styles.contentOverlay}>
        <Text style={[styles.scoreText, { color: colors.textPrimary }]}>{score}</Text>
        <Text style={[styles.scaleText, { color: colors.textMuted }]}>/ 100</Text>
        <View style={[styles.statusTag, { backgroundColor: statusColor + '20' }]}>
          <Text style={[styles.statusText, { color: statusColor }]}>{statusLabel}</Text>
        </View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
    alignSelf: 'center',
  },
  contentOverlay: {
    position: 'absolute',
    alignItems: 'center',
    justifyContent: 'center',
  },
  scoreText: {
    fontSize: 42,
    fontWeight: '800',
    letterSpacing: -1,
  },
  scaleText: {
    fontSize: 12,
    fontWeight: '600',
    marginTop: -4,
  },
  statusTag: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 12,
    marginTop: 6,
  },
  statusText: {
    fontSize: 12,
    fontWeight: '700',
  },
});
