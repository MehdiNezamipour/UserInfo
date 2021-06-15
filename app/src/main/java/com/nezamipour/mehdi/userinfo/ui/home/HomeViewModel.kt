package com.nezamipour.mehdi.userinfo.ui.home

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    @ExperimentalPagingApi
    private val pagingUser: Flow<PagingData<User>> = repository.getResult()

    @ExperimentalPagingApi
    fun getPagingUser(): Flow<PagingData<User>> {
        return pagingUser
    }

}