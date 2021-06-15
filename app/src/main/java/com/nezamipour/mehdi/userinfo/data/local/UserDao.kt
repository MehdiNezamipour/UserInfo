package com.nezamipour.mehdi.userinfo.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nezamipour.mehdi.userinfo.data.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertAll(users: List<User>)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_table")
    fun getPagingSource(): PagingSource<Int, User>

    @Update
    suspend fun updateAll(users: List<User>)

}