package com.nezamipour.mehdi.userinfo.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nezamipour.mehdi.userinfo.data.model.RemoteKey
import com.nezamipour.mehdi.userinfo.data.model.User

interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKey>)

    @Query("DELETE FROM remote_key")
    suspend fun deleteAll()

    @Query("SELECT * FROM remote_key WHERE id=:id")
    suspend fun getRemoteKey(id: Int): RemoteKey


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAll(remoteKeys: List<RemoteKey>)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(remoteKey: RemoteKey)


}
