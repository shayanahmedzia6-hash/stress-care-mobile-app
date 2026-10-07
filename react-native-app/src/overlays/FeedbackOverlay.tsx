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

export const FeedbackOverlay: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode, showToast } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [rating, setRating] = useState(5);
  const [category, setCategory] = useState('Sensor Accuracy');
  const [comment, setComment] = useState('');

  const categories = ['Sensor Accuracy', 'Intervention Quality', 'Wearable Battery', 'UI / Experience'];

  const handleSubmit = () => {
    showToast('Clinical feedback submitted to medical telemetry engineering team!');
    closeOverlay();
  };

  return (
    <Modal
      visible={activeOverlay === 'FEEDBACK'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View style={styles.headerLeft}>
              <Feather name="message-square" size={20} color={colors.primary} />
              <Text style={[styles.title, { color: colors.textPrimary }]}>Submit Clinical Feedback</Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={22} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
            Help our bio-medical engineering team refine stress detection algorithms and intervention workflows.
          </Text>

          {/* Star Rating */}
          <Text style={[styles.sectionLabel, { color: colors.textPrimary }]}>Experience Rating</Text>
          <View style={styles.starsRow}>
            {[1, 2, 3, 4, 5].map((star) => (
              <TouchableOpacity key={star} onPress={() => setRating(star)}>
                <Ionicons
                  name={star <= rating ? 'star' : 'star-outline'}
                  size={32}
                  color={star <= rating ? '#f59e0b' : colors.cardBorder}
                />
              </TouchableOpacity>
            ))}
          </View>

          {/* Category Chips */}
          <Text style={[styles.sectionLabel, { color: colors.textPrimary, marginTop: 14 }]}>
            Feedback Topic
          </Text>
          <View style={styles.chipsRow}>
            {categories.map((cat) => {
              const isPicked = cat === category;
              return (
                <TouchableOpacity
                  key={cat}
                  style={[
                    styles.chip,
                    {
                      backgroundColor: isPicked ? colors.primary : colors.surfaceSoft,
                      borderColor: isPicked ? colors.primary : colors.cardBorder,
                    },
                  ]}
                  onPress={() => setCategory(cat)}
                >
                  <Text style={[styles.chipText, { color: isPicked ? '#ffffff' : colors.textPrimary }]}>
                    {cat}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>

          {/* Comment Box */}
          <Text style={[styles.sectionLabel, { color: colors.textPrimary, marginTop: 14 }]}>
            Comments & Observations
          </Text>
          <TextInput
            style={[
              styles.textInput,
              {
                backgroundColor: colors.surfaceSoft,
                borderColor: colors.cardBorder,
                color: colors.textPrimary,
              },
            ]}
            placeholder="Describe your feedback, detected anomalies, or feature suggestions..."
            placeholderTextColor={colors.textMuted}
            multiline
            value={comment}
            onChangeText={setComment}
          />

          <TouchableOpacity
            style={[styles.submitButton, { backgroundColor: colors.primary }]}
            onPress={handleSubmit}
          >
            <Text style={styles.submitButtonText}>Submit Clinical Report</Text>
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
    marginBottom: 16,
    lineHeight: 16,
  },
  sectionLabel: {
    fontSize: 13,
    fontWeight: '700',
    marginBottom: 8,
  },
  starsRow: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: 6,
  },
  chipsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    marginBottom: 6,
  },
  chip: {
    paddingHorizontal: 12,
    paddingVertical: 7,
    borderRadius: 12,
    borderWidth: 1,
  },
  chipText: {
    fontSize: 12,
    fontWeight: '600',
  },
  textInput: {
    borderRadius: 14,
    padding: 12,
    height: 90,
    borderWidth: 1,
    textAlignVertical: 'top',
    fontSize: 13,
    marginBottom: 18,
  },
  submitButton: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
  },
  submitButtonText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
