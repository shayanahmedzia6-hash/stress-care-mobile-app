import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  FlatList,
  ActivityIndicator,
} from 'react-native';
import { Feather, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';
import { DiscoveredDevice } from '../types';

export const BlePairingModal: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode, vitals, toggleBandConnection } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [isScanning, setIsScanning] = useState(false);
  const [devices, setDevices] = useState<DiscoveredDevice[]>([
    { id: '1', name: 'StressCare Band X1 (Active)', rssi: -64, batteryLevel: 88 },
    { id: '2', name: 'StressCare Prototype B2', rssi: -78, batteryLevel: 42 },
    { id: '3', name: 'Empatica E4 BioSensor', rssi: -85, batteryLevel: 95 },
  ]);

  const handleScan = () => {
    setIsScanning(true);
    setTimeout(() => {
      setIsScanning(false);
      setDevices((prev) => [
        ...prev,
        { id: String(Date.now()), name: 'StressCare Band X2 (Discovered)', rssi: -71, batteryLevel: 75 },
      ]);
    }, 2000);
  };

  return (
    <Modal
      visible={activeOverlay === 'BLE_PAIRING'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.modalHeader}>
            <View style={styles.headerTitleRow}>
              <MaterialCommunityIcons name="bluetooth" size={22} color={colors.primary} />
              <Text style={[styles.modalTitle, { color: colors.textPrimary }]}>
                Bluetooth Device Pairing
              </Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={20} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <Text style={[styles.modalSub, { color: colors.textSecondary }]}>
            Ensure your StressCare wearable band is powered on and within 5 meters.
          </Text>

          {/* Scan Action */}
          <TouchableOpacity
            style={[styles.scanButton, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}
            onPress={handleScan}
            disabled={isScanning}
          >
            {isScanning ? (
              <ActivityIndicator size="small" color={colors.primary} />
            ) : (
              <Feather name="refresh-cw" size={16} color={colors.primary} />
            )}
            <Text style={[styles.scanButtonText, { color: colors.primary }]}>
              {isScanning ? 'Scanning BLE Advertisements...' : 'Scan for Nearby Wearables'}
            </Text>
          </TouchableOpacity>

          {/* Devices List */}
          <Text style={[styles.listHeading, { color: colors.textMuted }]}>
            AVAILABLE SENSORS ({devices.length})
          </Text>

          <FlatList
            data={devices}
            keyExtractor={(item) => item.id}
            style={styles.deviceList}
            renderItem={({ item }) => {
              const isCurrent = item.name.includes('Active');
              return (
                <View
                  style={[
                    styles.deviceItem,
                    {
                      borderColor: isCurrent ? colors.primary : colors.cardBorder,
                      backgroundColor: isCurrent ? colors.surfaceSoft : 'transparent',
                    },
                  ]}
                >
                  <View style={{ flex: 1 }}>
                    <Text style={[styles.deviceItemName, { color: colors.textPrimary }]}>
                      {item.name}
                    </Text>
                    <Text style={[styles.deviceItemSpecs, { color: colors.textMuted }]}>
                      RSSI: {item.rssi} dBm • Battery: {item.batteryLevel}%
                    </Text>
                  </View>

                  <TouchableOpacity
                    style={[
                      styles.connectBtn,
                      {
                        backgroundColor: isCurrent ? colors.accent : colors.primary,
                      },
                    ]}
                    onPress={toggleBandConnection}
                  >
                    <Text style={styles.connectBtnText}>
                      {isCurrent ? (vitals.isBandConnected ? 'Connected' : 'Reconnect') : 'Pair'}
                    </Text>
                  </TouchableOpacity>
                </View>
              );
            }}
          />

          <TouchableOpacity
            style={[styles.closeDoneBtn, { backgroundColor: colors.primary }]}
            onPress={closeOverlay}
          >
            <Text style={styles.closeDoneText}>Done</Text>
          </TouchableOpacity>
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
    padding: 20,
    maxHeight: '80%',
    borderWidth: 1,
  },
  modalHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  headerTitleRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: '700',
  },
  modalSub: {
    fontSize: 12,
    marginBottom: 16,
    lineHeight: 16,
  },
  scanButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    paddingVertical: 12,
    borderRadius: 14,
    borderWidth: 1,
    marginBottom: 16,
  },
  scanButtonText: {
    fontSize: 13,
    fontWeight: '600',
  },
  listHeading: {
    fontSize: 11,
    fontWeight: '700',
    marginBottom: 8,
    letterSpacing: 0.5,
  },
  deviceList: {
    maxHeight: 220,
  },
  deviceItem: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 12,
    borderRadius: 12,
    borderWidth: 1,
    marginBottom: 8,
  },
  deviceItemName: {
    fontSize: 14,
    fontWeight: '600',
  },
  deviceItemSpecs: {
    fontSize: 11,
    marginTop: 2,
  },
  connectBtn: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 10,
  },
  connectBtnText: {
    color: '#ffffff',
    fontSize: 12,
    fontWeight: '700',
  },
  closeDoneBtn: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
    marginTop: 16,
  },
  closeDoneText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
