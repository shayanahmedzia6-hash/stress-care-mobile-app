package com.example.model

data class PssQuestion(
    val id: Int,
    val text: String,
    val isReverseScored: Boolean = false
)

val PSS_OPTIONS = listOf(
    "Never" to 0,
    "Almost Never" to 1,
    "Sometimes" to 2,
    "Fairly Often" to 3,
    "Very Often" to 4
)

val PSS_QUESTIONS = listOf(
    PssQuestion(1, "In the last month, how often have you been upset because of something that happened unexpectedly?", false),
    PssQuestion(2, "In the last month, how often have you felt that you were unable to control the important things in your life?", false),
    PssQuestion(3, "In the last month, how often have you felt nervous and stressed?", false),
    PssQuestion(4, "In the last month, how often have you felt confident about your ability to handle your personal problems?", true),
    PssQuestion(5, "In the last month, how often have you felt that things were going your way?", true),
    PssQuestion(6, "In the last month, how often have you found that you could not cope with all the things that you had to do?", false),
    PssQuestion(7, "In the last month, how often have you been able to control irritations in your life?", true),
    PssQuestion(8, "In the last month, how often have you felt that you were on top of things?", true),
    PssQuestion(9, "In the last month, how often have you been angered because of things that happened that were outside of your control?", false),
    PssQuestion(10, "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?", false)
)

fun calculatePssScore(answers: Map<Int, Int>): Int {
    var total = 0
    PSS_QUESTIONS.forEach { q ->
        val raw = answers[q.id] ?: 0
        val actual = if (q.isReverseScored) 4 - raw else raw
        total += actual
    }
    return total
}

fun getPssCategory(score: Int): String {
    return when {
        score <= 13 -> "Low Stress"
        score <= 26 -> "Moderate Stress"
        else -> "High Perceived Stress"
    }
}
