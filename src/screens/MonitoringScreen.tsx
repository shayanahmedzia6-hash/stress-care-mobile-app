import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
} from 'react-native';
import { Ionicons, Feather, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';
import { SparklineChart } from '../components/SparklineChart';

export const MonitoringScreen: React.FC = () => {
  const { vitals, isDarkMode, openOverlay, showToast } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [sensorPPG, setSensorPPG] = useState(true);
  const [sensorGSR, setSensorGSR] = useState(true);
  const [sensorTemp, setSensorTemp] = useState(true);
  const [sensorAccel, setSensorAccel] = useState(true);

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      showsVerticalScrollIndicator={false}
    >
      {/* Stream Status Header */}
      <View style={[styles.statusCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.statusTopRow}>
          <View style={styles.deviceInfo}>
            <MaterialCommunityIcons name="bluetooth" size={20} color={colors.primary} />
            <View>
              <Text style={[styles.deviceName, { color: colors.textPrimary }]}>{vitals.deviceName}</Text>
              <Text style={[styles.deviceSub, { color: colors.textSecondary }]}>
                {vitals.isBandConnected ? 'Streaming Real-Time Packets' : 'Disconnected'}
              </Text>
            </View>
          </View>
          <TouchableOpacity
            style={[styles.pairButton, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
            onPress={() => openOverlay('BLE_PAIRING')}
          >
            <Text style={[styles.pairButtonText, { color: colors.primary }]}>BLE Settings</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.statsRow}>
          <View style={styles.statBox}>
            <Text style={[styles.statValue, { color: colors.textPrimary }]}>50 Hz</Text>
            <Text style={[styles.statLabel, { color: colors.textMuted }]}>Sampling Rate</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={[styles.statValue, { color: colors.textPrimary }]}>-64 dBm</Text>
            <Text style={[styles.statLabel, { color: colors.textMuted }]}>BLE RSSI</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={[styles.statValue, { color: colors.textPrimary }]}>{vitals.batteryPercent}%</Text>
            <Text style={[styles.statLabel, { color: colors.textMuted }]}>Band Battery</Text>
          </View>
          <View style={styles.statBox}>
            <Text style={[styles.statValue, { color: colors.accent }]}>0.01%</Text>
            <Text style={[styles.statLabel, { color: colors.textMuted }]}>Packet Drop</Text>
          </View>
        </View>
      </View>

      {/* Real-time Photoplethysmography (PPG) Waveform */}
      <View style={[styles.waveformCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.waveHeader}>
          <View>
            <Text style={[styles.waveTitle, { color: colors.textPrimary }]}>PPG Cardiac Pulse Wave</Text>
            <Text style={[styles.waveSub, { color: colors.textSecondary }]}>
              Infrared / Red optical reflection absorption
            </Text>
          </View>
          <Text style={[styles.currentMetric, { color: '#ef4444' }]}>{vitals.heartRate} BPM</Text>
        </View>
        <SparklineChart
          data={[70, 72, 75, 82, 68, 71, 74, 80, 69, 73, vitals.heartRate]}
          color="#ef4444"
          height={65}
        />
      </View>

      {/* Real-time Electrodermal GSR Waveform */}
      <View style={[styles.waveformCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.waveHeader}>
          <View>
            <Text style={[styles.waveTitle, { color: colors.textPrimary }]}>Galvanic Skin Response (GSR)</Text>
            <Text style={[styles.waveSub, { color: colors.textSecondary }]}>
              Sympathetic eccrine sweat gland conductance
            </Text>
          </View>
          <Text style={[styles.currentMetric, { color: '#0284c7' }]}>{vitals.gsrMicrosiemens} μS</Text>
        </View>
        <SparklineChart
          data={[2.1, 2.2, 2.3, 2.2, 2.4, 2.5, 2.3, 2.4, vitals.gsrMicrosiemens]}
          color="#0284c7"
          height={65}
        />
      </View>

      {/* Real-time Skin Thermistor */}
      <View style={[styles.waveformCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <View style={styles.waveHeader}>
          <View>
            <Text style={[styles.waveTitle, { color: colors.textPrimary }]}>Peripheral Skin Temperature</Text>
            <Text style={[styles.waveSub, { color: colors.textSecondary }]}>
              Micro-vasoconstriction stress indicator
            </Text>
          </View>
          <Text style={[styles.currentMetric, { color: '#f59e0b' }]}>{vitals.skinTempCelsius} °C</Text>
        </View>
        <SparklineChart
          data={[34.0, 34.1, 34.1, 34.2, 34.1, 34.2, vitals.skinTempCelsius]}
          color="#f59e0b"
          height={65}
        />
      </View>

      {/* Sensor Channel Hardware Switches */}
      <View style={[styles.sensorConfigCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <Text style={[styles.configTitle, { color: colors.textPrimary }]}>Active Telemetry Channels</Text>
        <Text style={[styles.configSub, { color: colors.textSecondary }]}>
          Toggle individual hardware sensors to optimize wearable battery life
        </Text>

        <View style={styles.switchRow}>
          <View>
            <Text style={[styles.switchLabel, { color: colors.textPrimary }]}>PPG Optical Pulse Sensor</Text>
            <Text style={[styles.switchSub, { color: colors.textMuted }]}>Continuous Heart Rate & HRV</Text>
          </View>
          <Switch
            value={sensorPPG}
            onValueChange={setSensorPPG}
            thumbColor={sensorPPG ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>

        <View style={styles.switchRow}>
          <View>
            <Text style={[styles.switchLabel, { color: colors.textPrimary }]}>GSR Conductance Electrodes</Text>
            <Text style={[styles.switchSub, { color: colors.textMuted }]}>Phasic stress spikes & tonic arousal</Text>
          </View>
          <Switch
            value={sensorGSR}
            onValueChange={setSensorGSR}
            thumbColor={sensorGSR ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>

        <View style={styles.switchRow}>
          <View>
            <Text style={[styles.switchLabel, { color: colors.textPrimary }]}>Thermistor Sensor</Text>
            <Text style={[styles.switchSub, { color: colors.textMuted }]}>Skin temperature drift tracking</Text>
          </View>
          <Switch
            value={sensorTemp}
            onValueChange={setSensorTemp}
            thumbColor={sensorTemp ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>

        <View style={styles.switchRow}>
          <View>
            <Text style={[styles.switchLabel, { color: colors.textPrimary }]}>3-Axis Accelerometer (IMU)</Text>
            <Text style={[styles.switchSub, { color: colors.textMuted }]}>Motion artifact noise reduction</Text>
          </View>
          <Switch
            value={sensorAccel}
            onValueChange={setSensorAccel}
            thumbColor={sensorAccel ? colors.primary : '#ccc'}
            trackColor={{ false: '#767577', true: colors.primaryLight }}
          />
        </View>
      </View>

      {/* Recalibrate Baseline Card */}
      <TouchableOpacity
        style={[styles.calibPrompt, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
        onPress={() => openOverlay('CALIBRATION')}
        activeOpacity={0.8}
      >
        <Feather name="refresh-cw" size={20} color={colors.primary} />
        <View style={{ flex: 1 }}>
          <Text style={[styles.calibTitle, { color: colors.textPrimary }]}>Re-Calibrate Sensor Baseline</Text>
          <Text style={[styles.calibSub, { color: colors.textSecondary }]}>
            Run 60s resting baseline when switching wrist or environments.
          </Text>
        </View>
        <Feather name="chevron-right" size={18} color={colors.textMuted} />
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
  statusCard: {
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
  },
  statusTopRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 14,
  },
  deviceInfo: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
  },
  deviceName: {
    fontSize: 16,
    fontWeight: '700',
  },
  deviceSub: {
    fontSize: 11,
    marginTop: 2,
  },
  pairButton: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 12,
    borderWidth: 1,
  },
  pairButtonText: {
    fontSize: 12,
    fontWeight: '600',
  },
  statsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingTop: 12,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
  },
  statBox: {
    alignItems: 'center',
  },
  statValue: {
    fontSize: 14,
    fontWeight: '700',
  },
  statLabel: {
    fontSize: 10,
    marginTop: 2,
  },
  waveformCard: {
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
  },
  waveHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  waveTitle: {
    fontSize: 14,
    fontWeight: '700',
  },
  waveSub: {
    fontSize: 11,
    marginTop: 2,
  },
  currentMetric: {
    fontSize: 16,
    fontWeight: '800',
  },
  sensorConfigCard: {
    borderRadius: 18,
    padding: 18,
    borderWidth: 1,
  },
  configTitle: {
    fontSize: 16,
    fontWeight: '700',
  },
  configSub: {
    fontSize: 11,
    marginTop: 2,
    marginBottom: 14,
  },
  switchRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 10,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
  },
  switchLabel: {
    fontSize: 13,
    fontWeight: '600',
  },
  switchSub: {
    fontSize: 10,
    marginTop: 1,
  },
  calibPrompt: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 16,
    borderRadius: 16,
    borderWidth: 1,
    gap: 12,
  },
  calibTitle: {
    fontSize: 14,
    fontWeight: '700',
  },
  calibSub: {
    fontSize: 11,
    marginTop: 2,
  },
});
