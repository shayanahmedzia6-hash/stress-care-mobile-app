import React, { useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  ScrollView,
  TextInput,
} from 'react-native';
import { Feather } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

const pssQuestions = [
  'In the last month, how often have you been upset because of something that happened unexpectedly?',
  'In the last month, how often have you felt that you were unable to control the important things in your life?',
  'In the last month, how often have you felt nervous and stressed?',
  'In the last month, how often have you felt confident about your ability to handle your personal problems?',
  'In the last month, how often have you felt that things were going your way?',
  'In the last month, how often have you found that you could not cope with all the things that you had to do?',
  'In the last month, how often have you been able to control irritations in your life?',
  'In the last month, how often have you felt that you were on top of things?',
  'In the last month, how often have you been angered because of things that happened that were outside of your control?',
  'In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?',
];

const optionLabels = ['Never', 'Almost Never', 'Sometimes', 'Fairly Often', 'Very Often'];
const moodChoices = ['Calm & Balanced', 'Slightly Anxious', 'Exhausted', 'Overwhelmed', 'Focused'];

export const PssQuestionnaireOverlay: React.FC = () => {
  const { activeOverlay, closeOverlay, isDarkMode, submitPss } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  const [answers, setAnswers] = useState<Record<number, number>>({
    1: 1, 2: 2, 3: 2, 4: 3, 5: 3, 6: 1, 7: 3, 8: 3, 9: 1, 10: 1,
  });
  const [selectedMood, setSelectedMood] = useState('Calm & Balanced');
  const [notes, setNotes] = useState('');

  const handleSelect = (qIndex: number, score: number) => {
    setAnswers((prev) => ({ ...prev, [qIndex]: score }));
  };

  const handleSubmit = () => {
    submitPss(answers, selectedMood, notes);
    closeOverlay();
  };

  return (
    <Modal
      visible={activeOverlay === 'PSS_QUESTIONNAIRE'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View>
              <Text style={[styles.title, { color: colors.textPrimary }]}>
                PSS-10 Stress Questionnaire
              </Text>
              <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
                Validated clinical index of perceived stress (Cohen et al.)
              </Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={22} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <ScrollView style={styles.scrollArea} showsVerticalScrollIndicator={false}>
            {/* Mood selector */}
            <Text style={[styles.sectionHeading, { color: colors.textPrimary }]}>Current Subjective Mood</Text>
            <View style={styles.moodRow}>
              {moodChoices.map((mood) => {
                const isSelected = mood === selectedMood;
                return (
                  <TouchableOpacity
                    key={mood}
                    style={[
                      styles.moodChip,
                      {
                        backgroundColor: isSelected ? colors.primary : colors.surfaceSoft,
                        borderColor: isSelected ? colors.primary : colors.cardBorder,
                      },
                    ]}
                    onPress={() => setSelectedMood(mood)}
                  >
                    <Text
                      style={[
                        styles.moodChipText,
                        { color: isSelected ? '#ffffff' : colors.textPrimary },
                      ]}
                    >
                      {mood}
                    </Text>
                  </TouchableOpacity>
                );
              })}
            </View>

            {/* Questions list */}
            <Text style={[styles.sectionHeading, { color: colors.textPrimary, marginTop: 18 }]}>
              PSS-10 Assessment Questions
            </Text>

            {pssQuestions.map((q, idx) => {
              const qNum = idx + 1;
              const currentScore = answers[qNum] ?? 2;
              return (
                <View key={qNum} style={[styles.questionBox, { borderColor: colors.cardBorder }]}>
                  <Text style={[styles.questionText, { color: colors.textPrimary }]}>
                    {qNum}. {q}
                  </Text>
                  <View style={styles.optionsRow}>
                    {optionLabels.map((lbl, optIdx) => {
                      const isPicked = currentScore === optIdx;
                      return (
                        <TouchableOpacity
                          key={lbl}
                          style={[
                            styles.optionButton,
                            {
                              backgroundColor: isPicked ? colors.primary : colors.surfaceSoft,
                              borderColor: isPicked ? colors.primary : colors.cardBorder,
                            },
                          ]}
                          onPress={() => handleSelect(qNum, optIdx)}
                        >
                          <Text
                            style={[
                              styles.optionText,
                              { color: isPicked ? '#ffffff' : colors.textSecondary },
                            ]}
                          >
                            {optIdx}
                          </Text>
                        </TouchableOpacity>
                      );
                    })}
                  </View>
                  <View style={styles.optionLegendRow}>
                    <Text style={[styles.legendSub, { color: colors.textMuted }]}>0 = Never</Text>
                    <Text style={[styles.legendSub, { color: colors.textMuted }]}>4 = Very Often</Text>
                  </View>
                </View>
              );
            })}

            {/* Notes input */}
            <Text style={[styles.sectionHeading, { color: colors.textPrimary, marginTop: 14 }]}>
              Clinical Observations / Notes (Optional)
            </Text>
            <TextInput
              style={[
                styles.notesInput,
                {
                  backgroundColor: colors.surfaceSoft,
                  borderColor: colors.cardBorder,
                  color: colors.textPrimary,
                },
              ]}
              placeholder="e.g. Preparing for major presentation; sleep disrupted..."
              placeholderTextColor={colors.textMuted}
              multiline
              value={notes}
              onChangeText={setNotes}
            />

            <TouchableOpacity
              style={[styles.submitButton, { backgroundColor: colors.primary }]}
              onPress={handleSubmit}
            >
              <Text style={styles.submitButtonText}>Calculate Clinical Score & Save</Text>
            </TouchableOpacity>
          </ScrollView>
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
    maxHeight: '90%',
    borderWidth: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 12,
  },
  title: {
    fontSize: 18,
    fontWeight: '700',
  },
  subtitle: {
    fontSize: 11,
    marginTop: 2,
  },
  scrollArea: {
    maxHeight: 520,
  },
  sectionHeading: {
    fontSize: 13,
    fontWeight: '700',
    marginBottom: 8,
  },
  moodRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  moodChip: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 12,
    borderWidth: 1,
  },
  moodChipText: {
    fontSize: 12,
    fontWeight: '600',
  },
  questionBox: {
    padding: 12,
    borderRadius: 14,
    borderWidth: 1,
    marginBottom: 10,
  },
  questionText: {
    fontSize: 13,
    lineHeight: 18,
    fontWeight: '600',
    marginBottom: 10,
  },
  optionsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 6,
  },
  optionButton: {
    flex: 1,
    paddingVertical: 8,
    borderRadius: 8,
    borderWidth: 1,
    alignItems: 'center',
  },
  optionText: {
    fontSize: 13,
    fontWeight: '700',
  },
  optionLegendRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 6,
  },
  legendSub: {
    fontSize: 9,
  },
  notesInput: {
    borderRadius: 12,
    padding: 12,
    borderWidth: 1,
    height: 70,
    textAlignVertical: 'top',
    fontSize: 13,
  },
  submitButton: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
    marginVertical: 18,
  },
  submitButtonText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
