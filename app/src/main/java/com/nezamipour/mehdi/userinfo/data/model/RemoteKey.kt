package com.nezamipour.mehdi.userinfo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val nextKey: Int?,
    val prevKey: Int?
)

