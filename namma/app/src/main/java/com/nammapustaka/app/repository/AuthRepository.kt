package com.nammapustaka.app.repository

import com.nammapustaka.app.database.UserDao
import com.nammapustaka.app.database.toDomain
import com.nammapustaka.app.database.toEntity
import com.nammapustaka.app.models.User

class AuthRepository(
    private val userDao: UserDao,
) {
    suspend fun loginStudent(name: String, roll: String, pin: String): User? {
        return userDao.findStudentByCredentials(
            name = name.trim(),
            roll = roll.trim(),
            pin = pin.trim(),
        )?.toDomain()
    }

    suspend fun registerStudent(name: String, roll: String, pin: String): User? {
        if (userDao.studentRollExists(roll.trim())) {
            return null
        }

        val user = User(
            name = name.trim(),
            role = User.ROLE_STUDENT,
            roll = roll.trim(),
            pin = pin.trim(),
        )
        val id = userDao.insert(user.toEntity())
        return user.copy(id = id)
    }

    suspend fun loginTeacher(username: String, password: String): User? {
        if (username.trim() != teacherUsername || password.trim() != teacherPassword) {
            return null
        }

        return User(
            id = 0L,
            name = "Teacher Admin",
            role = User.ROLE_TEACHER,
            roll = null,
            pin = teacherPassword,
        )
    }

    companion object {
        const val teacherUsername = "admin"
        const val teacherPassword = "admin123"
    }
}
