package com.nezamipour.mehdi.userinfo.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nezamipour.mehdi.userinfo.data.local.AppDatabase
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.data.remote.ApiService
import com.nezamipour.mehdi.userinfo.paging.UserRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val PAGE_SIZE = 2

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase
) {

    @ExperimentalPagingApi
    fun getUsersFromMediator(): Flow<PagingData<User>> {
        val pagingSourceFactory = { database.userDao().getPagingSource() }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (2 * PAGE_SIZE),
                enablePlaceholders = false
            ),
            remoteMediator = UserRemoteMediator(database, apiService),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getDummyUsers() {
        database.userDao().getDummyUsers()
    }
}