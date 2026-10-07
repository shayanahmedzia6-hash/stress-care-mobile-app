import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons, Feather } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const Header: React.FC = () => {
  const { isDarkMode, toggleDarkMode, vitals, openOverlay } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <View style={[styles.container, { borderBottomColor: colors.cardBorder }]}>
      <View style={styles.leftRow}>
        <View style={[styles.logoIcon, { backgroundColor: colors.primary }]}>
          <Ionicons name="pulse" size={20} color="#ffffff" />
        </View>
        <View>
          <Text style={[styles.title, { color: colors.textPrimary }]}>StressCare</Text>
          <View style={styles.badgeRow}>
            <View
              style={[
                styles.dot,
                { backgroundColor: vitals.isBandConnected ? colors.accent : colors.danger },
              ]}
            />
            <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
              {vitals.isBandConnected ? `Band Connected (${vitals.batteryPercent}%)` : 'Disconnected'}
            </Text>
          </View>
        </View>
      </View>

      <View style={styles.rightActions}>
        <TouchableOpacity
          style={[styles.iconButton, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
          onPress={toggleDarkMode}
          accessibilityLabel="Toggle Dark Mode"
        >
          <Feather name={isDarkMode ? 'sun' : 'moon'} size={18} color={colors.primary} />
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.iconButton, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
          onPress={() => openOverlay('NOTIFICATIONS')}
          accessibilityLabel="Notifications"
        >
          <Feather name="bell" size={18} color={colors.primary} />
          <View style={[styles.notifBadge, { backgroundColor: colors.danger }]} />
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 14,
    borderBottomWidth: 1,
  },
  leftRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  logoIcon: {
    width: 38,
    height: 38,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 20,
    fontWeight: '700',
    letterSpacing: -0.3,
  },
  badgeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    marginTop: 2,
  },
  dot: {
    width: 8,
    height: 8,
    borderRadius: 4,
  },
  subtitle: {
    fontSize: 12,
    fontWeight: '500',
  },
  rightActions: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  iconButton: {
    width: 38,
    height: 38,
    borderRadius: 10,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
  },
  notifBadge: {
    width: 8,
    height: 8,
    borderRadius: 4,
    position: 'absolute',
    top: 6,
    right: 6,
  },
});
