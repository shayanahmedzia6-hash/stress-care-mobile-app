package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.PssAssessmentDao
import com.example.data.local.dao.SensorReadingDao
import com.example.data.local.dao.StressSessionDao
import com.example.data.local.dao.UserPreferencesDao
import com.example.data.local.entities.PssAssessmentEntity
import com.example.data.local.entities.SensorReadingEntity
import com.example.data.local.entities.StressSessionEntity
import com.example.data.local.entities.UserPreferencesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        SensorReadingEntity::class,
        StressSessionEntity::class,
        PssAssessmentEntity::class,
        UserPreferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StressCareDatabase : RoomDatabase() {

    abstract fun sensorReadingDao(): SensorReadingDao
    abstract fun stressSessionDao(): StressSessionDao
    abstract fun pssAssessmentDao(): PssAssessmentDao
    abstract fun userPreferencesDao(): UserPreferencesDao

    companion object {
        @Volatile
        private var INSTANCE: StressCareDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StressCareDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StressCareDatabase::class.java,
                    "stresscare_database.db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database)
                    }
                }
            }
        }

        private suspend fun populateInitialData(db: StressCareDatabase) {
            val userPrefsDao = db.userPreferencesDao()
            val sessionDao = db.stressSessionDao()
            val pssDao = db.pssAssessmentDao()

            userPrefsDao.savePreferences(
                UserPreferencesEntity(
                    userId = "user_default",
                    name = "Alex Morgan",
                    email = "alex.morgan@stresscare.ai",
                    phone = "+1 (555) 234-5678",
                    isPremium = true,
                    planName = "StressCare Pro (Annual)",
                    autoCloudSync = true,
                    vibrateOnHighStress = true,
                    migraineAlerts = true,
                    hrBaseline = 68,
                    gsrBaseline = 3.2f,
                    tempBaseline = 36.5f
                )
            )

            val now = System.currentTimeMillis()
            val oneDay = 24 * 60 * 60 * 1000L

            // Seed historical monitoring sessions
            val sampleSessions = listOf(
                StressSessionEntity(
                    startTime = now - (oneDay * 0) - (3 * 3600_000L),
                    endTime = now - (oneDay * 0) - (2 * 3600_000L),
                    durationSeconds = 3600,
                    avgStress = 62,
                    maxStress = 81,
                    avgHeartRate = 84,
                    label = "Afternoon Sprint Review",
                    isSynced = true
                ),
                StressSessionEntity(
                    startTime = now - (oneDay * 0) - (7 * 3600_000L),
                    endTime = now - (oneDay * 0) - (6 * 3600_000L),
                    durationSeconds = 3600,
                    avgStress = 38,
                    maxStress = 52,
                    avgHeartRate = 71,
                    label = "Morning Deep Focus",
                    isSynced = true
                ),
                StressSessionEntity(
                    startTime = now - (oneDay * 1) - (4 * 3600_000L),
                    endTime = now - (oneDay * 1) - (2 * 3600_000L),
                    durationSeconds = 7200,
                    avgStress = 58,
                    maxStress = 76,
                    avgHeartRate = 79,
                    label = "Client Presentation",
                    isSynced = true
                ),
                StressSessionEntity(
                    startTime = now - (oneDay * 2) - (5 * 3600_000L),
                    endTime = now - (oneDay * 2) - (3 * 3600_000L),
                    durationSeconds = 7200,
                    avgStress = 44,
                    maxStress = 65,
                    avgHeartRate = 73,
                    label = "Routine Desk Work",
                    isSynced = true
                ),
                StressSessionEntity(
                    startTime = now - (oneDay * 3) - (6 * 3600_000L),
                    endTime = now - (oneDay * 3) - (4 * 3600_000L),
                    durationSeconds = 7200,
                    avgStress = 72,
                    maxStress = 88,
                    avgHeartRate = 89,
                    label = "Deadline Crunch",
                    isSynced = true
                ),
                StressSessionEntity(
                    startTime = now - (oneDay * 4) - (3 * 3600_000L),
                    endTime = now - (oneDay * 4) - (2 * 3600_000L),
                    durationSeconds = 3600,
                    avgStress = 32,
                    maxStress = 45,
                    avgHeartRate = 69,
                    label = "Weekend Rest & Reading",
                    isSynced = true
                ),
                StressSessionEntity(
                    startTime = now - (oneDay * 5) - (4 * 3600_000L),
                    endTime = now - (oneDay * 5) - (3 * 3600_000L),
                    durationSeconds = 3600,
                    avgStress = 29,
                    maxStress = 40,
                    avgHeartRate = 67,
                    label = "Nature Walk & Meditation",
                    isSynced = true
                )
            )
            sessionDao.insertAll(sampleSessions)

            // Seed initial PSS assessment
            pssDao.insertAssessment(
                PssAssessmentEntity(
                    timestamp = now - (oneDay * 2),
                    score = 18,
                    category = "Moderate Stress",
                    answersJson = "[2, 1, 3, 2, 2, 2, 2, 2, 1, 1]",
                    moodRating = "Moderate",
                    userNotes = "Work load was higher this week due to upcoming deliverables.",
                    isSynced = true
                )
            )
        }
    }
}
