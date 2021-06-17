package com.nezamipour.mehdi.userinfo.ui.detail

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nezamipour.mehdi.userinfo.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) :
    ViewModel() {

    val user: MutableLiveData<User> = MutableLiveData()

    fun insertOrReplaceBookmark(userId: Int, bookmarkState: Boolean) {
        sharedPreferences.edit().putBoolean(userId.toString(), bookmarkState).apply()
    }

    fun getBookmarkState(): Boolean {
        return sharedPreferences.getBoolean(user.value?.id.toString(), false)
    }

}