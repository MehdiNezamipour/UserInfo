package com.nezamipour.mehdi.userinfo.paging

import com.nezamipour.mehdi.userinfo.data.model.User

interface UserClickListener {

    fun onUserClicked(user: User)
}