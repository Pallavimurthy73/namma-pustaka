package com.nammapustaka.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nammapustaka.app.models.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val role: String,
    val roll: String? = null,
    val pin: String,
)

fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    role = role,
    roll = roll,
    pin = pin,
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    role = role,
    roll = roll,
    pin = pin,
)
