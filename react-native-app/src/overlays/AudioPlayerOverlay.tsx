import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  Modal,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import { Feather, Ionicons, MaterialCommunityIcons } from '@expo/vector-icons';
import { useStressCare } from '../context/StressCareContext';
import { LightColors, DarkColors } from '../theme/colors';

export const AudioPlayerOverlay: React.FC = () => {
  const {
    activeOverlay,
    closeOverlay,
    isDarkMode,
    audioPlaying,
    currentSoundtrack,
    soundtracks,
    toggleAudioPlay,
    selectSoundtrack,
    audioVolume,
    setAudioVolume,
  } = useStressCare();
  const colors = isDarkMode ? DarkColors : LightColors;

  return (
    <Modal
      visible={activeOverlay === 'AUDIO_PLAYER'}
      animationType="slide"
      transparent
      onRequestClose={closeOverlay}
    >
      <View style={styles.backdrop}>
        <View style={[styles.modalCard, { backgroundColor: colors.cardBackground, borderColor: colors.cardBorder }]}>
          <View style={styles.header}>
            <View style={styles.headerLeft}>
              <Ionicons name="headset" size={22} color={colors.primary} />
              <Text style={[styles.title, { color: colors.textPrimary }]}>
                Binaural Sound Therapy
              </Text>
            </View>
            <TouchableOpacity onPress={closeOverlay}>
              <Feather name="x" size={22} color={colors.textSecondary} />
            </TouchableOpacity>
          </View>

          <Text style={[styles.subtitle, { color: colors.textSecondary }]}>
            Use stereo headphones for optimal brainwave entrainment into alpha and theta relaxation states.
          </Text>

          {/* Current Playing Album Art Card */}
          <View style={[styles.playerHero, { backgroundColor: colors.surfaceSoft }]}>
            <View style={[styles.discIcon, { backgroundColor: colors.primary }]}>
              <MaterialCommunityIcons
                name={audioPlaying ? 'music-note' : 'music-note-outline'}
                size={36}
                color="#ffffff"
              />
            </View>
            <Text style={[styles.currentTrackTitle, { color: colors.textPrimary }]}>
              {currentSoundtrack}
            </Text>
            <Text style={[styles.currentTrackStatus, { color: audioPlaying ? colors.accent : colors.textMuted }]}>
              {audioPlaying ? '● Playing Frequency Audio' : 'Paused'}
            </Text>

            {/* Playback Controls */}
            <View style={styles.controlsRow}>
              <TouchableOpacity
                style={styles.skipBtn}
                onPress={() => {
                  const idx = soundtracks.indexOf(currentSoundtrack);
                  const prev = soundtracks[(idx - 1 + soundtracks.length) % soundtracks.length];
                  selectSoundtrack(prev);
                }}
              >
                <Feather name="skip-back" size={20} color={colors.textPrimary} />
              </TouchableOpacity>

              <TouchableOpacity
                style={[styles.playBtn, { backgroundColor: colors.primary }]}
                onPress={toggleAudioPlay}
              >
                <Ionicons
                  name={audioPlaying ? 'pause' : 'play'}
                  size={26}
                  color="#ffffff"
                  style={{ marginLeft: audioPlaying ? 0 : 2 }}
                />
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.skipBtn}
                onPress={() => {
                  const idx = soundtracks.indexOf(currentSoundtrack);
                  const next = soundtracks[(idx + 1) % soundtracks.length];
                  selectSoundtrack(next);
                }}
              >
                <Feather name="skip-forward" size={20} color={colors.textPrimary} />
              </TouchableOpacity>
            </View>

            {/* Volume Toggles */}
            <View style={styles.volumeRow}>
              <Feather name="volume-1" size={16} color={colors.textMuted} />
              <TouchableOpacity
                style={[styles.volChip, audioVolume === 0.3 && { backgroundColor: colors.primary }]}
                onPress={() => setAudioVolume(0.3)}
              >
                <Text style={{ color: audioVolume === 0.3 ? '#fff' : colors.textSecondary, fontSize: 11, fontWeight: '700' }}>
                  Low (30%)
                </Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.volChip, audioVolume === 0.75 && { backgroundColor: colors.primary }]}
                onPress={() => setAudioVolume(0.75)}
              >
                <Text style={{ color: audioVolume === 0.75 ? '#fff' : colors.textSecondary, fontSize: 11, fontWeight: '700' }}>
                  Med (75%)
                </Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={[styles.volChip, audioVolume === 1.0 && { backgroundColor: colors.primary }]}
                onPress={() => setAudioVolume(1.0)}
              >
                <Text style={{ color: audioVolume === 1.0 ? '#fff' : colors.textSecondary, fontSize: 11, fontWeight: '700' }}>
                  Max (100%)
                </Text>
              </TouchableOpacity>
              <Feather name="volume-2" size={16} color={colors.textMuted} />
            </View>
          </View>

          {/* Sound Library Playlist */}
          <Text style={[styles.libraryTitle, { color: colors.textMuted }]}>
            CURATED FREQUENCY TRACKS
          </Text>

          <ScrollView style={styles.trackList} showsVerticalScrollIndicator={false}>
            {soundtracks.map((track) => {
              const isSelected = track === currentSoundtrack;
              return (
                <TouchableOpacity
                  key={track}
                  style={[
                    styles.trackItem,
                    {
                      borderColor: isSelected ? colors.primary : colors.cardBorder,
                      backgroundColor: isSelected ? colors.surfaceSoft : 'transparent',
                    },
                  ]}
                  onPress={() => selectSoundtrack(track)}
                >
                  <View style={styles.trackItemLeft}>
                    <Ionicons
                      name={isSelected && audioPlaying ? 'volume-high' : 'musical-note'}
                      size={18}
                      color={isSelected ? colors.primary : colors.textMuted}
                    />
                    <Text
                      style={[
                        styles.trackItemName,
                        {
                          color: isSelected ? colors.primary : colors.textPrimary,
                          fontWeight: isSelected ? '700' : '500',
                        },
                      ]}
                    >
                      {track}
                    </Text>
                  </View>
                  {isSelected && (
                    <Text style={[styles.activeTag, { color: colors.primary }]}>Active</Text>
                  )}
                </TouchableOpacity>
              );
            })}
          </ScrollView>

          <TouchableOpacity
            style={[styles.doneButton, { backgroundColor: colors.primary }]}
            onPress={closeOverlay}
          >
            <Text style={styles.doneButtonText}>Close Studio</Text>
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
    maxHeight: '85%',
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
  playerHero: {
    borderRadius: 18,
    padding: 18,
    alignItems: 'center',
    marginBottom: 18,
  },
  discIcon: {
    width: 68,
    height: 68,
    borderRadius: 34,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 12,
  },
  currentTrackTitle: {
    fontSize: 16,
    fontWeight: '700',
    textAlign: 'center',
  },
  currentTrackStatus: {
    fontSize: 12,
    marginTop: 4,
    fontWeight: '600',
  },
  controlsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 24,
    marginVertical: 14,
  },
  skipBtn: {
    padding: 8,
  },
  playBtn: {
    width: 54,
    height: 54,
    borderRadius: 27,
    alignItems: 'center',
    justifyContent: 'center',
    elevation: 3,
  },
  volumeRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
    marginTop: 6,
  },
  volChip: {
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
    backgroundColor: 'rgba(0,0,0,0.06)',
  },
  libraryTitle: {
    fontSize: 11,
    fontWeight: '700',
    letterSpacing: 0.5,
    marginBottom: 10,
  },
  trackList: {
    maxHeight: 180,
  },
  trackItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 12,
    borderRadius: 12,
    borderWidth: 1,
    marginBottom: 8,
  },
  trackItemLeft: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    flex: 1,
  },
  trackItemName: {
    fontSize: 13,
  },
  activeTag: {
    fontSize: 11,
    fontWeight: '700',
  },
  doneButton: {
    paddingVertical: 14,
    borderRadius: 14,
    alignItems: 'center',
    marginTop: 14,
  },
  doneButtonText: {
    color: '#ffffff',
    fontSize: 14,
    fontWeight: '700',
  },
});
