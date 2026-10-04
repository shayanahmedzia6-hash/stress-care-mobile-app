package com.example.data.repository

import com.example.data.local.StressCareDatabase
import com.example.data.local.entities.PssAssessmentEntity
import com.example.data.local.entities.SensorReadingEntity
import com.example.data.local.entities.StressSessionEntity
import com.example.data.local.entities.UserPreferencesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StressCareRepository(private val database: StressCareDatabase) {

    private val sensorReadingDao = database.sensorReadingDao()
    private val stressSessionDao = database.stressSessionDao()
    private val pssAssessmentDao = database.pssAssessmentDao()
    private val userPreferencesDao = database.userPreferencesDao()

    val allSessions: Flow<List<StressSessionEntity>> = stressSessionDao.getAllSessions()
    val allPssAssessments: Flow<List<PssAssessmentEntity>> = pssAssessmentDao.getAllAssessments()
    val latestPssAssessment: Flow<PssAssessmentEntity?> = pssAssessmentDao.getLatestAssessment()
    val userPreferences: Flow<UserPreferencesEntity?> = userPreferencesDao.getUserPreferences()

    suspend fun saveReading(reading: SensorReadingEntity) = withContext(Dispatchers.IO) {
        sensorReadingDao.insertReading(reading)
    }

    suspend fun saveSession(session: StressSessionEntity) = withContext(Dispatchers.IO) {
        stressSessionDao.insertSession(session)
    }

    suspend fun savePssAssessment(assessment: PssAssessmentEntity) = withContext(Dispatchers.IO) {
        pssAssessmentDao.insertAssessment(assessment)
    }

    suspend fun updatePreferences(preferences: UserPreferencesEntity) = withContext(Dispatchers.IO) {
        userPreferencesDao.savePreferences(preferences)
    }

    suspend fun updateBaselines(hrBaseline: Int, gsrBaseline: Float, tempBaseline: Float) = withContext(Dispatchers.IO) {
        val current = UserPreferencesEntity(
            userId = "user_default",
            hrBaseline = hrBaseline,
            gsrBaseline = gsrBaseline,
            tempBaseline = tempBaseline,
            lastSyncTimestamp = System.currentTimeMillis()
        )
        userPreferencesDao.savePreferences(current)
    }

    // Cloud Synchronization simulation / execution:
    // Pushes all local unsynced records to cloud and updates sync timestamps
    suspend fun syncWithCloud(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            delay(1200) // Realistic cloud round-trip
            val unsyncedReadings = sensorReadingDao.getUnsyncedReadings()
            val unsyncedSessions = stressSessionDao.getUnsyncedSessions()
            val unsyncedAssessments = pssAssessmentDao.getUnsyncedAssessments()

            val syncedCount = unsyncedReadings.size + unsyncedSessions.size + unsyncedAssessments.size

            if (unsyncedReadings.isNotEmpty()) {
                sensorReadingDao.markAsSynced(unsyncedReadings.map { it.id })
            }
            if (unsyncedSessions.isNotEmpty()) {
                stressSessionDao.markAsSynced(unsyncedSessions.map { it.id })
            }
            if (unsyncedAssessments.isNotEmpty()) {
                pssAssessmentDao.markAsSynced(unsyncedAssessments.map { it.id })
            }

            // Update user sync timestamp
            val now = System.currentTimeMillis()
            userPreferencesDao.savePreferences(
                UserPreferencesEntity(
                    userId = "user_default",
                    lastSyncTimestamp = now
                )
            )

            Result.success(syncedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
