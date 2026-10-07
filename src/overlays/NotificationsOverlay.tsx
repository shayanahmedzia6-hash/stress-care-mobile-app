import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import { Feather, Ionicons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

const notifications = [
  {
    id: '1',
    title: 'Baseline Autonomic Calibration Active',
    body: 'Continuous 50Hz sensor telemetry active. Baseline galvanic response established.',
    time: '10 min ago',
    type: 'info',
  },
  {
    id: '2',
    title: 'Mild Sympathetic Phasic Spike Detected',
    body: 'Heart rate climbed to 88 bpm during meeting. Autonomic recovery normalized within 4 minutes.',
    time: '2 hours ago',
    type: 'warning',
  },
  {
    id: '3',
    title: 'Cloud Biometric Database Synced',
    body: '74 offline SQLite sensor telemetry records synchronized successfully with Firestore.',
    time: 'Yesterday',
    type: 'success',
  },
  {
    id: '4',
    title: 'Evening Vagal Tone Breathing Prompt',
    body: 'Scheduled 2-minute box breathing session ready to assist melatonin onset.',
    time: 'Yesterday',
    type: 'info',
  },
];

export const NotificationsOverlay: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <Modal
      visible={activeOverlay === 'NOTIFICATIONS'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View style={styles.headerLeft}>
              <Feather name="bell" size={20} color={colors.primary} />
              <Text style={[styles.title, { color: colors.textPrimary }]}>Notification Center</Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={20} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <ScrollView style={styles.listArea} showsVerticalScrollIndicator={false}>
            {notifications.map((item) => (
              <View
                key={item.id}
                style={[
                  styles.notifCard,
                  {
                    backgroundColor: colors.surfaceSoft,
                    borderColor: colors.cardBorder,
                  },
                ]}
              >
                <View style={styles.notifHeader}>
                  <View style={styles.notifHeaderLeft}>
                    <Ionicons
                      name={
                        item.type === 'warning'
                          ? 'alert-circle'
                          : item.type === 'success'
                          ? 'checkmark-circle'
                          : 'information-circle'
                      }
                      size={18}
                      color={
                        item.type === 'warning'
                          ? colors.warning
                          : item.type === 'success'
                          ? colors.accent
                          : colors.primary
                      }
                    />
                    <Text style={[styles.notifTitle, { color: colors.textPrimary }]}>
                      {item.title}
                    </Text>
                  </View>
                  <Text style={[styles.notifTime, { color: colors.textMuted }]}>{item.time}</Text>
                </View>
                <Text style={[styles.notifBody, { color: colors.textSecondary }]}>{item.body}</Text>
              </View>
            ))}
          </ScrollView>

          <TouchableOpacity
            style={[styles.closeButton, { backgroundColor: colors.primary }]}
            onPress={closeOverlay}
          >
            <Text style={styles.closeButtonText}>Mark All as Read</Text>
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
    padding: 22,
    maxHeight: '75%',
    borderWidth: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
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
  listArea: {
    maxHeight: 380,
  },
  notifCard: {
    borderRadius: 14,
    padding: 14,
    borderWidth: 1,
    marginBottom: 10,
  },
  notifHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  notifHeaderLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 6,
    flex: 1,
  },
  notifTitle: {
    fontSize: 13,
    fontWeight: '700',
  },
  notifTime: {
    fontSize: 10,
  },
  notifBody: {
    fontSize: 11,
    lineHeight: 16,
  },
  closeButton: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
    marginTop: 12,
  },
  closeButtonText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
