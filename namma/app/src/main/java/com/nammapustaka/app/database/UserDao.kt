package com.nammapustaka.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query(
        """
        SELECT * FROM users
        WHERE LOWER(name) = LOWER(:name)
          AND roll = :roll
          AND pin = :pin
          AND role = 'student'
        LIMIT 1
        """,
    )
    suspend fun findStudentByCredentials(name: String, roll: String, pin: String): UserEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE roll = :roll AND role = 'student')")
    suspend fun studentRollExists(roll: String): Boolean

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Insert
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users WHERE role = 'student' ORDER BY name ASC")
    fun observeStudents(): Flow<List<UserEntity>>

    @Query("SELECT COUNT(*) FROM users")
    suspend fun countUsers(): Int
}
