package com.example

import com.example.audio.PRESET_AUDIO_TRACKS
import com.example.ble.StressCategory
import com.example.model.calculatePssScore
import com.example.model.getPssCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testPss10Calculation_allZeros() {
        // When all answers are 0, reverse-scored items (4, 5, 7, 8) become 4 each -> total = 16
        val answers = (1..10).associateWith { 0 }
        val score = calculatePssScore(answers)
        assertEquals(16, score)
    }

    @Test
    fun testPss10Calculation_allTwos() {
        // When all answers are 2, reverse-scored items remain 4 - 2 = 2 -> total = 20
        val answers = (1..10).associateWith { 2 }
        val score = calculatePssScore(answers)
        assertEquals(20, score)
        assertEquals("Moderate Stress", getPssCategory(score))
    }

    @Test
    fun testPss10Categories() {
        assertEquals("Low Stress", getPssCategory(0))
        assertEquals("Low Stress", getPssCategory(13))
        assertEquals("Moderate Stress", getPssCategory(14))
        assertEquals("Moderate Stress", getPssCategory(26))
        assertEquals("High Perceived Stress", getPssCategory(27))
        assertEquals("High Perceived Stress", getPssCategory(40))
    }

    @Test
    fun testAudioPresets() {
        assertEquals(6, PRESET_AUDIO_TRACKS.size)
        assertTrue(PRESET_AUDIO_TRACKS.any { it.title.contains("Rain") })
        assertTrue(PRESET_AUDIO_TRACKS.any { it.title.contains("Ocean") })
        assertTrue(PRESET_AUDIO_TRACKS.any { it.title.contains("Tibetan") })
    }

    @Test
    fun testStressCategoryEnum() {
        assertEquals("Low Stress", StressCategory.LOW.label)
        assertEquals("Moderate Stress", StressCategory.MODERATE.label)
        assertEquals("High Stress", StressCategory.HIGH.label)
    }
}
