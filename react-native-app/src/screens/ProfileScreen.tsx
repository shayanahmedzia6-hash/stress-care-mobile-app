import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  ActivityIndicator,
} from 'react-native';
import { Ionicons, Feather, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const ProfileScreen: React.FC = () => {
  const {
    isDarkMode,
    toggleDarkMode,
    vitals,
    openOverlay,
    isSyncing,
    triggerSync,
    showToast,
  } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [notifEnabled, setNotifEnabled] = useState(true);
  const [hapticEnabled, setHapticEnabled] = useState(true);

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      showsVerticalScrollIndicator={false}
    >
      {/* Profile Card */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.profileRow}>
          <View style={[styles.avatarBox, { backgroundColor: colors.primary }]}>
            <Text style={styles.avatarInitials}>SJ</Text>
          </View>
          <View style={{ flex: 1 }}>
            <Text style={[styles.userName, { color: colors.textPrimary }]}>Sarah Jenkins, M.D.</Text>
            <Text style={[styles.userRole, { color: colors.textSecondary }]}>
              ID: SC-92841 • Premium Health Tier
            </Text>
          </View>
          <TouchableOpacity
            style={[styles.accountBtn, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
            onPress={() => openOverlay('AUTH')}
          >
            <Text style={[styles.accountBtnText, { color: colors.primary }]}>Account</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Paired Device Card */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.deviceHeader}>
          <View style={styles.deviceHeaderLeft}>
            <MaterialCommunityIcons name="watch" size={24} color={colors.primary} />
            <View>
              <Text style={[styles.deviceTitle, { color: colors.textPrimary }]}>
                {vitals.deviceName}
              </Text>
              <Text style={[styles.deviceStatus, { color: colors.accent }]}>
                ● {vitals.isBandConnected ? 'Connected & Active' : 'Offline'}
              </Text>
            </View>
          </View>
          <View style={styles.batteryBadge}>
            <Ionicons name="battery-charging" size={16} color={colors.accent} />
            <Text style={[styles.batteryText, { color: colors.textPrimary }]}>
              {vitals.batteryPercent}%
            </Text>
          </View>
        </View>

        <View style={styles.deviceSpecsRow}>
          <Text style={[styles.specText, { color: colors.textMuted }]}>
            Firmware: v2.4.1 (BLE 5.2)
          </Text>
          <Text style={[styles.specText, { color: colors.textMuted }]}>
            MAC: C4:7F:51:8A:1E:09
          </Text>
        </View>

        <View style={styles.deviceButtonsRow}>
          <TouchableOpacity
            style={[styles.deviceActionBtn, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
            onPress={() => openOverlay('BLE_PAIRING')}
          >
            <Feather name="bluetooth" size={14} color={colors.primary} />
            <Text style={[styles.deviceActionText, { color: colors.primary }]}>
              Pair New Device
            </Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={[styles.deviceActionBtn, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
            onPress={() => openOverlay('CALIBRATION')}
          >
            <Feather name="sliders" size={14} color={colors.primary} />
            <Text style={[styles.deviceActionText, { color: colors.primary }]}>
              Calibrate Baseline
            </Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* App Preferences */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <Text style={[styles.sectionTitle, { color: colors.textPrimary }]}>Preferences & System</Text>

        {/* Dark Mode Switch */}
        <View style={styles.prefRow}>
          <View style={styles.prefLeft}>
            <Feather name={isDarkMode ? 'moon' : 'sun'} size={18} color={colors.primary} />
            <View>
              <Text style={[styles.prefLabel, { color: colors.textPrimary }]}>Dark Mode</Text>
              <Text style={[styles.prefSub, { color: colors.textMuted }]}>
                {isDarkMode ? 'Dark Navy Blue theme' : 'Crisp Pastel Sky Blue theme'}
              </Text>
            </View>
          </View>
          <Switch
            value={isDarkMode}
            onValueChange={toggleDarkMode}
            thumbColor={isDarkMode ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>

        {/* Real-time Notifications */}
        <View style={styles.prefRow}>
          <View style={styles.prefLeft}>
            <Feather name="bell" size={18} color={colors.primary} />
            <View>
              <Text style={[styles.prefLabel, { color: colors.textPrimary }]}>Stress Spike Alerts</Text>
              <Text style={[styles.prefSub, { color: colors.textMuted }]}>
                Instant notifications when stress exceeds 70
              </Text>
            </View>
          </View>
          <Switch
            value={notifEnabled}
            onValueChange={setNotifEnabled}
            thumbColor={notifEnabled ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>

        {/* Haptic Breathing Feedback */}
        <View style={styles.prefRow}>
          <View style={styles.prefLeft}>
            <MaterialCommunityIcons name="vibrate" size={18} color={colors.primary} />
            <View>
              <Text style={[styles.prefLabel, { color: colors.textPrimary }]}>Haptic Breathing Cues</Text>
              <Text style={[styles.prefSub, { color: colors.textMuted }]}>
                Gentle vibration pacing during box breathing
              </Text>
            </View>
          </View>
          <Switch
            value={hapticEnabled}
            onValueChange={setHapticEnabled}
            thumbColor={hapticEnabled ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>

        {/* Cloud Sync Button */}
        <TouchableOpacity
          style={[styles.syncButton, { backgroundColor: colors.primary }]}
          onPress={triggerSync}
          disabled={isSyncing}
        >
          {isSyncing ? (
            <ActivityIndicator size="small" color="#ffffff" />
          ) : (
            <Feather name="cloud" size={16} color="#ffffff" />
          )}
          <Text style={styles.syncButtonText}>
            {isSyncing ? 'Synchronizing Cloud Database...' : 'Sync Data with Cloud (Firestore)'}
          </Text>
        </TouchableOpacity>
      </View>

      {/* Quick Links & Feedback */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <TouchableOpacity
          style={styles.linkRow}
          onPress={() => openOverlay('NOTIFICATIONS')}
        >
          <Feather name="inbox" size={18} color={colors.primary} />
          <Text style={[styles.linkTitle, { color: colors.textPrimary }]}>Notification Center</Text>
          <Feather name="chevron-right" size={18} color={colors.textMuted} />
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.linkRow}
          onPress={() => openOverlay('FEEDBACK')}
        >
          <Feather name="message-square" size={18} color={colors.primary} />
          <Text style={[styles.linkTitle, { color: colors.textPrimary }]}>Submit Clinical Feedback</Text>
          <Feather name="chevron-right" size={18} color={colors.textMuted} />
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.linkRow}
          onPress={() => openOverlay('ONBOARDING')}
        >
          <Feather name="help-circle" size={18} color={colors.primary} />
          <Text style={[styles.linkTitle, { color: colors.textPrimary }]}>View App Tour & Tutorials</Text>
          <Feather name="chevron-right" size={18} color={colors.textMuted} />
        </TouchableOpacity>
      </View>

      {/* Medical Disclaimer */}
      <View style={[styles.disclaimerCard, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
        <Feather name="shield" size={16} color={colors.textMuted} />
        <Text style={[styles.disclaimerText, { color: colors.textSecondary }]}>
          StressCare is an investigational bio-telemetry application designed for wellness and stress biofeedback. It is not intended to diagnose, cure, mitigate, treat, or prevent any medical disease or psychological condition.
        </Text>
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
    paddingBottom: 28,
    gap: 16,
  },
  card: {
    borderRadius: 18,
    padding: 18,
    borderWidth: 1,
  },
  profileRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 14,
  },
  avatarBox: {
    width: 48,
    height: 48,
    borderRadius: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  avatarInitials: {
    color: '#ffffff',
    fontSize: 18,
    fontWeight: '800',
  },
  userName: {
    fontSize: 16,
    fontWeight: '700',
  },
  userRole: {
    fontSize: 11,
    marginTop: 2,
  },
  accountBtn: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 10,
    borderWidth: 1,
  },
  accountBtnText: {
    fontSize: 12,
    fontWeight: '600',
  },
  deviceHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  deviceHeaderLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  deviceTitle: {
    fontSize: 16,
    fontWeight: '700',
  },
  deviceStatus: {
    fontSize: 11,
    fontWeight: '600',
    marginTop: 2,
  },
  batteryBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
    backgroundColor: 'rgba(16, 185, 129, 0.1)',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 10,
  },
  batteryText: {
    fontSize: 12,
    fontWeight: '700',
  },
  deviceSpecsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginVertical: 8,
  },
  specText: {
    fontSize: 11,
  },
  deviceButtonsRow: {
    flexDirection: 'row',
    gap: 10,
    marginTop: 10,
  },
  deviceActionBtn: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 6,
    paddingVertical: 10,
    borderRadius: 12,
    borderWidth: 1,
  },
  deviceActionText: {
    fontSize: 12,
    fontWeight: '600',
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '700',
    marginBottom: 12,
  },
  prefRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 12,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
  },
  prefLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    flex: 1,
  },
  prefLabel: {
    fontSize: 14,
    fontWeight: '600',
  },
  prefSub: {
    fontSize: 11,
    marginTop: 2,
  },
  syncButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    paddingVertical: 12,
    borderRadius: 12,
    marginTop: 16,
  },
  syncButtonText: {
    color: '#ffffff',
    fontSize: 13,
    fontWeight: '700',
  },
  linkRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 12,
    gap: 12,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
  },
  linkTitle: {
    flex: 1,
    fontSize: 14,
    fontWeight: '600',
  },
  disclaimerCard: {
    flexDirection: 'row',
    padding: 14,
    borderRadius: 14,
    borderWidth: 1,
    gap: 10,
  },
  disclaimerText: {
    flex: 1,
    fontSize: 10,
    lineHeight: 14,
  },
});
