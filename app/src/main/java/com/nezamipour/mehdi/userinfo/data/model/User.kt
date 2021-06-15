package com.nezamipour.mehdi.userinfo.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val first_name: String,
    val last_name: String,
    val avatar: String,
    val email: String
) : Parcelable
