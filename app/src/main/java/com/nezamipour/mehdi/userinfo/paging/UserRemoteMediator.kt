package com.nezamipour.mehdi.userinfo.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nezamipour.mehdi.userinfo.data.local.AppDatabase
import com.nezamipour.mehdi.userinfo.data.model.RemoteKey
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class UserRemoteMediator @Inject constructor(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService,
    private val initialPage: Int = 1
) : RemoteMediator<Int, User>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        val pageKeyData = getPageKeyData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }
        return try {
            val response = apiService.getUsers(page)
            val isEndOfList = response.body()?.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeyDao().deleteAll()
                    appDatabase.userDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList == true) null else page + 1
                val keys = response.body()?.map {
                    RemoteKey(it.id, nextKey = nextKey, prevKey = prevKey)
                }
                if (keys != null) {
                    appDatabase.remoteKeyDao().insertAll(keys)
                }
                response.body()?.let { appDatabase.userDao().insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList == true)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPageKeyData(loadType: LoadType, state: PagingState<Int, User>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                null
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, User>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.let { user ->
                appDatabase.remoteKeyDao().getRemoteKey(user.id)
            }
    }


}