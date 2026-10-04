package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.PssAssessmentEntity
import com.example.data.local.entities.SensorReadingEntity
import com.example.data.local.entities.StressSessionEntity
import com.example.data.local.entities.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SensorReadingDao {
    @Query("SELECT * FROM sensor_readings ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentReadings(limit: Int = 100): Flow<List<SensorReadingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: SensorReadingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<SensorReadingEntity>)

    @Query("SELECT * FROM sensor_readings WHERE isSynced = 0")
    suspend fun getUnsyncedReadings(): List<SensorReadingEntity>

    @Query("UPDATE sensor_readings SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)
}

@Dao
interface StressSessionDao {
    @Query("SELECT * FROM stress_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<StressSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: StressSessionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sessions: List<StressSessionEntity>)

    @Query("SELECT * FROM stress_sessions WHERE isSynced = 0")
    suspend fun getUnsyncedSessions(): List<StressSessionEntity>

    @Query("UPDATE stress_sessions SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)
}

@Dao
interface PssAssessmentDao {
    @Query("SELECT * FROM pss_assessments ORDER BY timestamp DESC")
    fun getAllAssessments(): Flow<List<PssAssessmentEntity>>

    @Query("SELECT * FROM pss_assessments ORDER BY timestamp DESC LIMIT 1")
    fun getLatestAssessment(): Flow<PssAssessmentEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: PssAssessmentEntity): Long

    @Query("SELECT * FROM pss_assessments WHERE isSynced = 0")
    suspend fun getUnsyncedAssessments(): List<PssAssessmentEntity>

    @Query("UPDATE pss_assessments SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<Long>)
}

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE userId = :userId LIMIT 1")
    fun getUserPreferences(userId: String = "user_default"): Flow<UserPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreferences(preferences: UserPreferencesEntity)

    @Update
    suspend fun updatePreferences(preferences: UserPreferencesEntity)
}
