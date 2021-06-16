package com.nezamipour.mehdi.userinfo.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.nezamipour.mehdi.userinfo.data.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getPagingSource(): PagingSource<Int, User>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(users: List<User>)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(users: User)

    @Query("SELECT * FROM user_table")
    suspend fun getDummyUsers(): List<User>


}