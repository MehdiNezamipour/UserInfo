package com.nezamipour.mehdi.userinfo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nezamipour.mehdi.userinfo.data.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

}