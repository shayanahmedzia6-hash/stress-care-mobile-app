import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  ScrollView,
  KeyboardAvoidingView,
  Platform,
} from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Feather, Ionicons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const AuthScreen: React.FC = () => {
  const { isDarkMode, completeAuth, showToast } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [isRegister, setIsRegister] = useState(false);
  const [fullName, setFullName] = useState('Alex Morgan');
  const [email, setEmail] = useState('alex.morgan@stresscare.ai');
  const [phone, setPhone] = useState('+1 (555) 234-5678');
  const [password, setPassword] = useState('password123');
  const [confirmPassword, setConfirmPassword] = useState('password123');
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [agreeToTerms, setAgreeToTerms] = useState(true);

  const gradient = isDarkMode
    ? (['#0F172A', '#0B0F19'] as const)
    : (['#EAF3FF', '#F5FAFF', '#FFFFFF'] as const);

  const handleSubmit = () => {
    if (!email.trim() || !password.trim()) {
      showToast('Please enter email and password');
      return;
    }
    if (isRegister) {
      if (!fullName.trim()) {
        showToast('Please enter your full name');
        return;
      }
      if (password !== confirmPassword) {
        showToast('Passwords do not match');
        return;
      }
      if (!agreeToTerms) {
        showToast('Please agree to the terms to continue');
        return;
      }
    }
    completeAuth(fullName.trim() || 'Alex Morgan');
    showToast(isRegister ? 'Account created successfully!' : `Welcome back, ${fullName.split(' ')[0]}!`);
  };

  return (
    <LinearGradient colors={gradient} style={styles.container}>
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <ScrollView
          contentContainerStyle={styles.scroll}
          keyboardShouldPersistTaps="handled"
          showsVerticalScrollIndicator={false}
        >
          <View style={styles.header}>
            <View style={styles.logoRow}>
              <View style={[styles.logoMark, { backgroundColor: colors.primary }]}>
                <Ionicons name="heart" size={16} color="#fff" />
              </View>
              <Text style={[styles.logoText, { color: colors.textPrimary }]}>StressCare</Text>
            </View>
          </View>

          <View style={[styles.heroIconWrap, { backgroundColor: colors.surfaceSoft }]}>
            <Ionicons name="shield-checkmark" size={36} color={colors.primary} />
          </View>

          <Text style={[styles.title, { color: colors.textPrimary }]}>
            {isRegister ? 'Create your account' : 'Welcome back'}
          </Text>
          <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
            {isRegister
              ? 'Secure medical cloud sync for biometric & PSS data.'
              : 'Sign in to continue your stress & recovery journey.'}
          </Text>

          <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
            {isRegister && (
              <>
                <Text style={[styles.label, { color: colors.textPrimary }]}>Full Name</Text>
                <View style={[styles.inputRow, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
                  <Feather name="user" size={18} color={colors.textMuted} />
                  <TextInput
                    style={[styles.input, { color: colors.textPrimary }]}
                    value={fullName}
                    onChangeText={setFullName}
                    placeholder="Full name"
                    placeholderTextColor={colors.textMuted}
                  />
                </View>
              </>
            )}

            <Text style={[styles.label, { color: colors.textPrimary }]}>Email</Text>
            <View style={[styles.inputRow, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
              <Feather name="mail" size={18} color={colors.textMuted} />
              <TextInput
                style={[styles.input, { color: colors.textPrimary }]}
                value={email}
                onChangeText={setEmail}
                keyboardType="email-address"
                autoCapitalize="none"
                placeholder="Email"
                placeholderTextColor={colors.textMuted}
              />
            </View>

            {isRegister && (
              <>
                <Text style={[styles.label, { color: colors.textPrimary }]}>Phone</Text>
                <View style={[styles.inputRow, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
                  <Feather name="phone" size={18} color={colors.textMuted} />
                  <TextInput
                    style={[styles.input, { color: colors.textPrimary }]}
                    value={phone}
                    onChangeText={setPhone}
                    keyboardType="phone-pad"
                    placeholder="Phone"
                    placeholderTextColor={colors.textMuted}
                  />
                </View>
              </>
            )}

            <Text style={[styles.label, { color: colors.textPrimary }]}>Password</Text>
            <View style={[styles.inputRow, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
              <Feather name="lock" size={18} color={colors.textMuted} />
              <TextInput
                style={[styles.input, { color: colors.textPrimary }]}
                value={password}
                onChangeText={setPassword}
                secureTextEntry={!passwordVisible}
                placeholder="Password"
                placeholderTextColor={colors.textMuted}
              />
              <TouchableOpacity onPress={() => setPasswordVisible((v) => !v)}>
                <Feather name={passwordVisible ? 'eye-off' : 'eye'} size={18} color={colors.textMuted} />
              </TouchableOpacity>
            </View>

            {isRegister && (
              <>
                <Text style={[styles.label, { color: colors.textPrimary }]}>Confirm Password</Text>
                <View style={[styles.inputRow, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
                  <Feather name="lock" size={18} color={colors.textMuted} />
                  <TextInput
                    style={[styles.input, { color: colors.textPrimary }]}
                    value={confirmPassword}
                    onChangeText={setConfirmPassword}
                    secureTextEntry={!passwordVisible}
                    placeholder="Confirm password"
                    placeholderTextColor={colors.textMuted}
                  />
                </View>

                <TouchableOpacity
                  style={styles.termsRow}
                  onPress={() => setAgreeToTerms((v) => !v)}
                >
                  <View
                    style={[
                      styles.checkbox,
                      {
                        borderColor: colors.primary,
                        backgroundColor: agreeToTerms ? colors.primary : 'transparent',
                      },
                    ]}
                  >
                    {agreeToTerms && <Feather name="check" size={12} color="#fff" />}
                  </View>
                  <Text style={[styles.termsText, { color: colors.textSecondary }]}>
                    I agree to the Terms of Service and Privacy Policy
                  </Text>
                </TouchableOpacity>
              </>
            )}

            <TouchableOpacity
              style={[styles.primaryBtn, { backgroundColor: colors.primary }]}
              onPress={handleSubmit}
              activeOpacity={0.85}
            >
              <Text style={styles.primaryBtnText}>
                {isRegister ? 'Create Account' : 'Sign In'}
              </Text>
            </TouchableOpacity>

            <TouchableOpacity style={styles.switchRow} onPress={() => setIsRegister((v) => !v)}>
              <Text style={[styles.switchText, { color: colors.textSecondary }]}>
                {isRegister ? 'Already have an account? ' : "Don't have an account? "}
                <Text style={{ color: colors.primary, fontWeight: '700' }}>
                  {isRegister ? 'Sign In' : 'Register'}
                </Text>
              </Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </LinearGradient>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  scroll: {
    paddingHorizontal: 20,
    paddingTop: 24,
    paddingBottom: 40,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 20,
  },
  logoRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  logoMark: {
    width: 32,
    height: 32,
    borderRadius: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  logoText: {
    fontSize: 18,
    fontWeight: '800',
  },
  heroIconWrap: {
    width: 72,
    height: 72,
    borderRadius: 36,
    alignItems: 'center',
    justifyContent: 'center',
    alignSelf: 'center',
    marginBottom: 16,
  },
  title: {
    fontSize: 26,
    fontWeight: '800',
    textAlign: 'center',
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 13,
    textAlign: 'center',
    lineHeight: 18,
    marginBottom: 22,
  },
  card: {
    borderRadius: 20,
    borderWidth: 1,
    padding: 18,
  },
  label: {
    fontSize: 12,
    fontWeight: '700',
    marginBottom: 6,
    marginTop: 10,
  },
  inputRow: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 12,
    paddingHorizontal: 12,
    gap: 10,
  },
  input: {
    flex: 1,
    paddingVertical: 12,
    fontSize: 14,
  },
  termsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    marginTop: 14,
  },
  checkbox: {
    width: 20,
    height: 20,
    borderRadius: 6,
    borderWidth: 2,
    alignItems: 'center',
    justifyContent: 'center',
  },
  termsText: {
    flex: 1,
    fontSize: 12,
    lineHeight: 16,
  },
  primaryBtn: {
    marginTop: 20,
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
  },
  primaryBtnText: {
    color: '#fff',
    fontSize: 15,
    fontWeight: '700',
  },
  switchRow: {
    alignItems: 'center',
    marginTop: 16,
  },
  switchText: {
    fontSize: 13,
  },
});
