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
    private val initialPage: Int = 1,
) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

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
        try {
            val response = apiService.getUsers(page = page, perPage = state.config.pageSize)
            val isEndOfList = response.users.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeyDao().deleteAll()
                    appDatabase.userDao().deleteAll()
                }
                val prevKey = if (page == initialPage) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.users.map {
                    RemoteKey(it.id, nextKey = nextKey, prevKey = prevKey)
                }

                appDatabase.remoteKeyDao().insertAll(keys)
                response.users.let { appDatabase.userDao().insertAll(it) }
            }

            return MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getPageKeyData(loadType: LoadType, state: PagingState<Int, User>): Any {
        val pageKeyData = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: initialPage
            }
            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                val nextKey = remoteKey?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKey = getFirstRemoteKey(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
        return pageKeyData
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, User>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.let { user ->
                appDatabase.remoteKeyDao().getRemoteKey(user.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, User>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { userId ->
                appDatabase.remoteKeyDao().getRemoteKey(userId)
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, User>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { user -> appDatabase.remoteKeyDao().getRemoteKey(user.id) }
    }

}

