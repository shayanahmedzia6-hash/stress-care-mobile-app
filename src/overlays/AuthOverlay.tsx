import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  TextInput,
} from 'react-native';
import { Feather, Ionicons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const AuthOverlay: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode, showToast, completeAuth } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [isRegister, setIsRegister] = useState(false);
  const [email, setEmail] = useState('sarah.jenkins@hospital.org');
  const [password, setPassword] = useState('password123');

  const handleAuthSubmit = () => {
    completeAuth('Sarah Jenkins');
    showToast(isRegister ? 'Account created and synced with cloud!' : 'Signed in as Sarah Jenkins, M.D.');
    closeOverlay();
  };

  return (
    <Modal
      visible={activeOverlay === 'AUTH'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View style={styles.headerLeft}>
              <Ionicons name="lock-closed" size={20} color={colors.primary} />
              <Text style={[styles.title, { color: colors.textPrimary }]}>
                {isRegister ? 'Create Medical Account' : 'Clinical Bio-Cloud Sign In'}
              </Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={22} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
            Secure end-to-end encrypted backup of physiological recordings & PSS assessments.
          </Text>

          {/* Form */}
          <Text style={[styles.inputLabel, { color: colors.textPrimary }]}>Professional Email</Text>
          <TextInput
            style={[
              styles.inputField,
              {
                backgroundColor: colors.surfaceSoft,
                borderColor: colors.cardBorder,
                color: colors.textPrimary,
              },
            ]}
            value={email}
            onChangeText={setEmail}
            keyboardType="email-address"
            autoCapitalize="none"
          />

          <Text style={[styles.inputLabel, { color: colors.textPrimary, marginTop: 12 }]}>
            Password
          </Text>
          <TextInput
            style={[
              styles.inputField,
              {
                backgroundColor: colors.surfaceSoft,
                borderColor: colors.cardBorder,
                color: colors.textPrimary,
              },
            ]}
            value={password}
            onChangeText={setPassword}
            secureTextEntry
          />

          <TouchableOpacity
            style={[styles.primaryBtn, { backgroundColor: colors.primary }]}
            onPress={handleAuthSubmit}
          >
            <Text style={styles.primaryBtnText}>
              {isRegister ? 'Register Account' : 'Sign In'}
            </Text>
          </TouchableOpacity>

          {/* Switch Mode */}
          <TouchableOpacity
            style={styles.switchModeRow}
            onPress={() => setIsRegister(!isRegister)}
          >
            <Text style={[styles.switchText, { color: colors.textSecondary }]}>
              {isRegister ? 'Already have an account? ' : "Don't have an account yet? "}
              <Text style={{ color: colors.primary, fontWeight: '700' }}>
                {isRegister ? 'Sign In' : 'Register'}
              </Text>
            </Text>
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
    marginBottom: 18,
    lineHeight: 16,
  },
  inputLabel: {
    fontSize: 12,
    fontWeight: '700',
    marginBottom: 6,
  },
  inputField: {
    borderRadius: 12,
    paddingHorizontal: 14,
    paddingVertical: 12,
    borderWidth: 1,
    fontSize: 14,
  },
  primaryBtn: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
    marginTop: 20,
  },
  primaryBtnText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
  switchModeRow: {
    alignItems: 'center',
    marginTop: 16,
    paddingVertical: 4,
  },
  switchText: {
    fontSize: 13,
  },
});
