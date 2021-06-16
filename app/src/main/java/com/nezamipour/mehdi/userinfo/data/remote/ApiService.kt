package com.nezamipour.mehdi.userinfo.data.remote

import com.nezamipour.mehdi.userinfo.data.model.UserResult
import com.nezamipour.mehdi.userinfo.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(Constants.USERS)
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UserResult

    @GET(Constants.USERS + "{id}")
    suspend fun getSingle(@Path("id") id: Int): Response<User>

}