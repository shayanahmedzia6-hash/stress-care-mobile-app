package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensor_readings")
data class SensorReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val heartRate: Int,
    val gsrMicrosiemens: Float,
    val skinTempCelsius: Float,
    val accelMagnitude: Float,
    val stressScore: Int, // 0-100
    val stressLevel: String, // "LOW", "MODERATE", "HIGH"
    val isSynced: Boolean = false
)

@Entity(tableName = "stress_sessions")
data class StressSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val durationSeconds: Int,
    val avgStress: Int,
    val maxStress: Int,
    val avgHeartRate: Int,
    val label: String, // e.g. "Morning Work Session", "Post-Lunch Walk", "Meeting Stress"
    val isSynced: Boolean = false
)

@Entity(tableName = "pss_assessments")
data class PssAssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val score: Int, // 0 to 40
    val category: String, // "Low Stress", "Moderate Stress", "High Perceived Stress"
    val answersJson: String,
    val moodRating: String, // "Low", "Moderate", "High"
    val userNotes: String,
    val isSynced: Boolean = false
)

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val userId: String = "user_default",
    val name: String = "Alex Morgan",
    val email: String = "alex.morgan@stresscare.ai",
    val phone: String = "+1 (555) 234-5678",
    val isPremium: Boolean = true,
    val planName: String = "StressCare Pro (Annual)",
    val autoCloudSync: Boolean = true,
    val vibrateOnHighStress: Boolean = true,
    val migraineAlerts: Boolean = true,
    val hrBaseline: Int = 68,
    val gsrBaseline: Float = 3.2f,
    val tempBaseline: Float = 36.5f,
    val lastSyncTimestamp: Long = System.currentTimeMillis() - 120_000 // 2 mins ago
)
