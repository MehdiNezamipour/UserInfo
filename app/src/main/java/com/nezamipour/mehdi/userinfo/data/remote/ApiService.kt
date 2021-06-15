package com.nezamipour.mehdi.userinfo.data.remote

import com.nezamipour.mehdi.userinfo.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET
    suspend fun getUsers(@Query("page") page: Int): Response<List<User>>

    @GET("/{id}")
    suspend fun getSingle(@Path("id") id: Int): Response<User>

}