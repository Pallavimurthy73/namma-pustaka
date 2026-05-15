package com.nammapustaka.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, BookEntity::class, BorrowTransactionEntity::class, ReviewEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class NammaPustakaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun borrowTransactionDao(): BorrowTransactionDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: NammaPustakaDatabase? = null

        fun getInstance(context: Context): NammaPustakaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NammaPustakaDatabase::class.java,
                    "namma_pustaka.db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}
