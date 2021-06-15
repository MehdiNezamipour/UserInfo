package com.nezamipour.mehdi.userinfo.data.model

import com.google.gson.annotations.SerializedName

data class UserResult(
    @SerializedName("data")
    val users: List<User>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)