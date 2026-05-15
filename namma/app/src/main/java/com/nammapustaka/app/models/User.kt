package com.nammapustaka.app.models

data class User(
    val id: Long = 0L,
    val name: String,
    val role: String,
    val roll: String? = null,
    val pin: String,
) {
    val isTeacher: Boolean
        get() = role == ROLE_TEACHER

    val isStudent: Boolean
        get() = role == ROLE_STUDENT

    val initials: String
        get() {
            val parts = name.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
            return when {
                parts.isEmpty() -> "NP"
                parts.size == 1 -> parts.first().take(1).uppercase()
                else -> "${parts.first().take(1)}${parts.last().take(1)}".uppercase()
            }
        }

    companion object {
        const val ROLE_STUDENT = "student"
        const val ROLE_TEACHER = "teacher"
    }
}
