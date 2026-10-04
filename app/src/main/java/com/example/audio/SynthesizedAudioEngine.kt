package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class AudioTrackItem(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val durationSeconds: Int = 180,
    val frequencyHz: Double = 432.0,
    val themeColorHex: Long = 0xFF06B6D4
)

val PRESET_AUDIO_TRACKS = listOf(
    AudioTrackItem(
        id = "track_rain",
        title = "Rain & Thunder",
        category = "Deep Relaxation",
        description = "Gentle rain precipitation with distant low-frequency rolling thunder rumble.",
        durationSeconds = 300,
        frequencyHz = 120.0,
        themeColorHex = 0xFF38BDF8
    ),
    AudioTrackItem(
        id = "track_ocean",
        title = "Ocean Waves",
        category = "Rhythmic Calm",
        description = "Binaural surf swells synchronized to 0.1Hz resonant human breathing frequency.",
        durationSeconds = 240,
        frequencyHz = 216.0,
        themeColorHex = 0xFF06B6D4
    ),
    AudioTrackItem(
        id = "track_tibetan",
        title = "Tibetan Bowls",
        category = "Meditation & Focus",
        description = "Harmonic 432Hz bronze singing bowl resonance with sustained soothing overtone.",
        durationSeconds = 360,
        frequencyHz = 432.0,
        themeColorHex = 0xFF8B5CF6
    ),
    AudioTrackItem(
        id = "track_piano",
        title = "Soft Piano & Strings",
        category = "Anxiety Relief",
        description = "Gentle acoustic piano progressions embedded with relaxing theta brainwave pulses.",
        durationSeconds = 210,
        frequencyHz = 528.0,
        themeColorHex = 0xFFA855F7
    ),
    AudioTrackItem(
        id = "track_forest",
        title = "Forest Birds & Stream",
        category = "Nature Immersion",
        description = "Chirping morning birds along a babbling mountain creek in pine woodlands.",
        durationSeconds = 280,
        frequencyHz = 350.0,
        themeColorHex = 0xFF10B981
    ),
    AudioTrackItem(
        id = "track_whitenoise",
        title = "Pink & White Noise",
        category = "Sensory Isolation",
        description = "Soft pink spectrum acoustic blanket to mask background environmental stressors.",
        durationSeconds = 300,
        frequencyHz = 200.0,
        themeColorHex = 0xFFF59E0B
    )
)

class SynthesizedAudioEngine(private val scope: CoroutineScope) {

    private val sampleRate = 22050
    private var audioTrack: AudioTrack? = null
    private var playbackJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentTrackIndex = MutableStateFlow(0)
    val currentTrackIndex: StateFlow<Int> = _currentTrackIndex.asStateFlow()

    private val _currentTrack = MutableStateFlow(PRESET_AUDIO_TRACKS[0])
    val currentTrack: StateFlow<AudioTrackItem> = _currentTrack.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f) // 0.0 to 1.0
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _volume = MutableStateFlow(0.75f)
    val volume: StateFlow<Float> = _volume.asStateFlow()

    private val _visualizerAmplitudes = MutableStateFlow(List(16) { 0.2f })
    val visualizerAmplitudes: StateFlow<List<Float>> = _visualizerAmplitudes.asStateFlow()

    fun selectTrack(index: Int) {
        val safeIdx = index.coerceIn(0, PRESET_AUDIO_TRACKS.size - 1)
        _currentTrackIndex.value = safeIdx
        _currentTrack.value = PRESET_AUDIO_TRACKS[safeIdx]
        _elapsedSeconds.value = 0
        _playbackProgress.value = 0f
        if (_isPlaying.value) {
            startAudioStream()
        }
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pause()
        } else {
            play()
        }
    }

    fun play() {
        _isPlaying.value = true
        startAudioStream()
    }

    fun pause() {
        _isPlaying.value = false
        stopAudioStream()
    }

    fun nextTrack() {
        val nextIdx = (_currentTrackIndex.value + 1) % PRESET_AUDIO_TRACKS.size
        selectTrack(nextIdx)
    }

    fun previousTrack() {
        val prevIdx = if (_currentTrackIndex.value > 0) _currentTrackIndex.value - 1 else PRESET_AUDIO_TRACKS.size - 1
        selectTrack(prevIdx)
    }

    fun setVolume(vol: Float) {
        val safeVol = vol.coerceIn(0f, 1f)
        _volume.value = safeVol
        audioTrack?.setVolume(safeVol)
    }

    fun seekTo(progress: Float) {
        val safeProg = progress.coerceIn(0f, 1f)
        _playbackProgress.value = safeProg
        _elapsedSeconds.value = (safeProg * _currentTrack.value.durationSeconds).toInt()
    }

    private fun startAudioStream() {
        stopAudioStream()

        val minBufSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        val bufSize = (minBufSize * 2).coerceAtLeast(4096)

        try {
            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack?.setVolume(_volume.value)
            audioTrack?.play()
        } catch (e: Exception) {
            // AudioTrack init fallback
        }

        playbackJob = scope.launch(Dispatchers.Default) {
            val buffer = ShortArray(1024)
            var phase = 0.0
            var waveCycle = 0.0
            val track = _currentTrack.value
            val baseFreq = track.frequencyHz

            var secondCounter = 0
            while (isActive && _isPlaying.value) {
                // Synthesize smooth relaxing audio waveforms
                for (i in buffer.indices) {
                    val sample: Double = when (track.id) {
                        "track_rain" -> {
                            // Pink noise with soft rumble
                            val white = (Random.nextDouble() * 2.0 - 1.0)
                            val rumble = sin(phase * 0.1) * 0.4
                            (white * 0.3 + rumble * 0.5) * 0.35
                        }
                        "track_ocean" -> {
                            // Resonant surging wave envelope
                            waveCycle += (2.0 * PI * 0.12) / sampleRate
                            val envelope = (sin(waveCycle) + 1.0) * 0.5
                            val hiss = (Random.nextDouble() * 2.0 - 1.0) * envelope * 0.4
                            val lowSurge = sin(phase * 0.2) * envelope * 0.5
                            (hiss + lowSurge) * 0.4
                        }
                        "track_tibetan" -> {
                            // 432 Hz pure singing bowl overtone
                            val fundamental = sin(phase)
                            val harmonic = sin(phase * 2.005) * 0.35
                            val third = sin(phase * 3.01) * 0.15
                            val vibrato = (1.0 + 0.1 * sin(waveCycle * 0.2))
                            (fundamental + harmonic + third) * vibrato * 0.3
                        }
                        "track_piano" -> {
                            // Warm chime chord
                            val note1 = sin(phase)
                            val note2 = sin(phase * 1.25) * 0.6 // Major third
                            val note3 = sin(phase * 1.5) * 0.4  // Fifth
                            (note1 + note2 + note3) * 0.25
                        }
                        "track_forest" -> {
                            // Chirping intermittent sweeps with gentle breeze
                            val chirp = if ((sin(waveCycle * 0.5) > 0.7)) sin(phase * 2.5) * 0.35 else 0.0
                            val wind = (Random.nextDouble() * 2.0 - 1.0) * 0.15
                            chirp + wind
                        }
                        else -> {
                            // Pink/White noise
                            (Random.nextDouble() * 2.0 - 1.0) * 0.25
                        }
                    }

                    buffer[i] = (sample * 32767.0).toInt().coerceIn(-32767, 32767).toShort()
                    phase += (2.0 * PI * baseFreq) / sampleRate
                    if (phase > 2.0 * PI) phase -= 2.0 * PI
                }

                audioTrack?.write(buffer, 0, buffer.size)

                // Update visualizer animations
                val newAmps = List(16) { idx ->
                    val factor = sin(waveCycle * 3.0 + idx * 0.4).toFloat()
                    (0.2f + (factor + 1f) * 0.35f + Random.nextFloat() * 0.15f).coerceIn(0.1f, 1f)
                }
                _visualizerAmplitudes.value = newAmps

                secondCounter += buffer.size
                if (secondCounter >= sampleRate) {
                    secondCounter = 0
                    val totalSec = _currentTrack.value.durationSeconds
                    val newElapsed = (_elapsedSeconds.value + 1)
                    if (newElapsed >= totalSec) {
                        nextTrack()
                    } else {
                        _elapsedSeconds.value = newElapsed
                        _playbackProgress.value = newElapsed.toFloat() / totalSec.toFloat()
                    }
                }
            }
        }
    }

    private fun stopAudioStream() {
        playbackJob?.cancel()
        playbackJob = null
        try {
            audioTrack?.pause()
            audioTrack?.flush()
            audioTrack?.release()
        } catch (e: Exception) {
            // Safe cleanup
        }
        audioTrack = null
    }

    fun release() {
        stopAudioStream()
    }
}
