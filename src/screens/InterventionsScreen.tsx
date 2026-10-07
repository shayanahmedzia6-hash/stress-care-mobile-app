import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
} from 'react-native';
import { Ionicons, Feather, MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const InterventionsScreen: React.FC = () => {
  const { isDarkMode, openOverlay } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [expandedPmr, setExpandedPmr] = useState(false);
  const [expandedGrounding, setExpandedGrounding] = useState(false);

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.contentContainer}
      showsVerticalScrollIndicator={false}
    >
      {/* Box Breathing Hero Launcher */}
      <TouchableOpacity
        style={[styles.heroCard, { borderColor: colors.cardBorder }]}
        onPress={() => openOverlay('GUIDED_BREATHING')}
        activeOpacity={0.85}
      >
        <LinearGradient
          colors={['#10b981', '#047857']}
          style={styles.heroGradient}
        >
          <View style={styles.heroContent}>
            <View style={styles.heroIconBox}>
              <Ionicons name="leaf" size={24} color="#ffffff" />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={styles.heroTitle}>Guided Box Breathing (4-4-4-4)</Text>
              <Text style={styles.heroSub}>
                Clinically proven autonomic down-regulation to rapidly lower sympathetic cortisol spikes.
              </Text>
            </View>
          </View>
          <View style={styles.heroButton}>
            <Text style={styles.heroButtonText}>Begin 2-Min Session</Text>
            <Feather name="play" size={14} color="#047857" />
          </View>
        </LinearGradient>
      </TouchableOpacity>

      {/* Binaural Beats Sound Therapy */}
      <TouchableOpacity
        style={[styles.heroCard, { borderColor: colors.cardBorder }]}
        onPress={() => openOverlay('AUDIO_PLAYER')}
        activeOpacity={0.85}
      >
        <LinearGradient
          colors={['#2563eb', '#1e40af']}
          style={styles.heroGradient}
        >
          <View style={styles.heroContent}>
            <View style={styles.heroIconBox}>
              <Ionicons name="headset" size={24} color="#ffffff" />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={styles.heroTitle}>Binaural Frequency Soundscapes</Text>
              <Text style={styles.heroSub}>
                432 Hz healing resonance, 528 Hz DNA repair tone, and delta ocean waves for alpha brainwaves.
              </Text>
            </View>
          </View>
          <View style={styles.heroButton}>
            <Text style={styles.heroButtonText}>Open Sound Studio</Text>
            <Feather name="volume-2" size={14} color="#1e40af" />
          </View>
        </LinearGradient>
      </TouchableOpacity>

      {/* 5-4-3-2-1 Grounding Method Card */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <TouchableOpacity
          style={styles.cardHeader}
          onPress={() => setExpandedGrounding(!expandedGrounding)}
          activeOpacity={0.7}
        >
          <View style={styles.cardHeaderLeft}>
            <View style={[styles.iconCircle, { backgroundColor: '#8b5cf620' }]}>
              <MaterialCommunityIcons name="eye-outline" size={20} color="#8b5cf6" />
            </View>
            <View>
              <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>
                5-4-3-2-1 Sensory Grounding
              </Text>
              <Text style={[styles.cardSub, { color: colors.textSecondary }]}>
                Mindfulness technique for panic & acute cognitive overload
              </Text>
            </View>
          </View>
          <Feather
            name={expandedGrounding ? 'chevron-up' : 'chevron-down'}
            size={20}
            color={colors.textMuted}
          />
        </TouchableOpacity>

        {expandedGrounding && (
          <View style={styles.expandedContent}>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              👁️ <Text style={{ fontWeight: '700' }}>5 things you see:</Text> Look around and notice 5 details (shadows, textures, patterns).
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              ✋ <Text style={{ fontWeight: '700' }}>4 things you can feel:</Text> Feet on the ground, clothes against skin, table surface.
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              👂 <Text style={{ fontWeight: '700' }}>3 things you hear:</Text> Air conditioning, distant traffic, quiet hum.
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              👃 <Text style={{ fontWeight: '700' }}>2 things you smell:</Text> Fresh coffee, fresh air, wood.
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              👅 <Text style={{ fontWeight: '700' }}>1 thing you taste:</Text> Sip of water, mint, or lingering taste.
            </Text>
          </View>
        )}
      </View>

      {/* Progressive Muscle Relaxation (PMR) */}
      <View style={[styles.card, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
        <TouchableOpacity
          style={styles.cardHeader}
          onPress={() => setExpandedPmr(!expandedPmr)}
          activeOpacity={0.7}
        >
          <View style={styles.cardHeaderLeft}>
            <View style={[styles.iconCircle, { backgroundColor: '#f59e0b20' }]}>
              <MaterialCommunityIcons name="arm-flex-outline" size={20} color="#f59e0b" />
            </View>
            <View>
              <Text style={[styles.cardTitle, { color: colors.textPrimary }]}>
                Progressive Muscle Relaxation
              </Text>
              <Text style={[styles.cardSub, { color: colors.textSecondary }]}>
                Jacobson tension-release somatic somatic biofeedback
              </Text>
            </View>
          </View>
          <Feather
            name={expandedPmr ? 'chevron-up' : 'chevron-down'}
            size={20}
            color={colors.textMuted}
          />
        </TouchableOpacity>

        {expandedPmr && (
          <View style={styles.expandedContent}>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              1. <Text style={{ fontWeight: '700' }}>Hands & Forearms:</Text> Clench fists tight for 5 seconds. Exhale and release completely for 10 seconds.
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              2. <Text style={{ fontWeight: '700' }}>Shoulders & Neck:</Text> Shrug shoulders towards ears. Feel the contraction. Let them drop heavily.
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              3. <Text style={{ fontWeight: '700' }}>Facial Muscles:</Text> Scrunch eyes and forehead tightly. Smooth out wrinkles and relax jaw.
            </Text>
            <Text style={[styles.stepText, { color: colors.textPrimary }]}>
              4. <Text style={{ fontWeight: '700' }}>Abdomen & Core:</Text> Tighten abdominal wall. Release and feel warm abdominal blood flow.
            </Text>
          </View>
        )}
      </View>

      {/* Somatic Vagus Nerve Quick Tip */}
      <View style={[styles.tipCard, { backgroundColor: colors.surfaceSoft, borderColor: colors.cardBorder }]}>
        <Ionicons name="sparkles" size={18} color={colors.primary} />
        <View style={{ flex: 1 }}>
          <Text style={[styles.tipTitle, { color: colors.textPrimary }]}>
            Vagus Nerve Reset: Physiological Sigh
          </Text>
          <Text style={[styles.tipSub, { color: colors.textSecondary }]}>
            Take two quick inhales through the nose, followed by one long, slow sigh exhale through the mouth. Repeat 3 times to immediately lower heart rate.
          </Text>
        </View>
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
  heroCard: {
    borderRadius: 20,
    overflow: 'hidden',
    borderWidth: 1,
    elevation: 3,
  },
  heroGradient: {
    padding: 20,
  },
  heroContent: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    gap: 14,
    marginBottom: 16,
  },
  heroIconBox: {
    width: 44,
    height: 44,
    borderRadius: 14,
    backgroundColor: 'rgba(255,255,255,0.2)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  heroTitle: {
    fontSize: 17,
    fontWeight: '700',
    color: '#ffffff',
  },
  heroSub: {
    fontSize: 12,
    color: 'rgba(255,255,255,0.85)',
    marginTop: 4,
    lineHeight: 17,
  },
  heroButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
    backgroundColor: '#ffffff',
    paddingVertical: 10,
    borderRadius: 12,
  },
  heroButtonText: {
    fontSize: 13,
    fontWeight: '700',
    color: '#1a2b4e',
  },
  card: {
    borderRadius: 18,
    padding: 16,
    borderWidth: 1,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  cardHeaderLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    flex: 1,
  },
  iconCircle: {
    width: 40,
    height: 40,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  cardTitle: {
    fontSize: 15,
    fontWeight: '700',
  },
  cardSub: {
    fontSize: 11,
    marginTop: 2,
  },
  expandedContent: {
    marginTop: 14,
    paddingTop: 12,
    borderTopWidth: 1,
    borderTopColor: 'rgba(0,0,0,0.05)',
    gap: 8,
  },
  stepText: {
    fontSize: 13,
    lineHeight: 18,
  },
  tipCard: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    padding: 16,
    borderRadius: 16,
    borderWidth: 1,
    gap: 12,
  },
  tipTitle: {
    fontSize: 13,
    fontWeight: '700',
  },
  tipSub: {
    fontSize: 11,
    marginTop: 3,
    lineHeight: 16,
  },
});
