package com.nezamipour.mehdi.userinfo.paging

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.nezamipour.mehdi.userinfo.R
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.databinding.ListItemLayoutBinding
import javax.inject.Inject

class UserAdapter @Inject constructor(private val sharedPreferences: SharedPreferences) :
    PagingDataAdapter<User, UserViewHolder>(DIFF_CALLBACK) {

    lateinit var userClickListener: UserClickListener

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.recycler_items_anim)
        getItem(position)?.let {
            holder.bind(it, sharedPreferences.getBoolean(it.id.toString(), false))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ListItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), userClickListener = userClickListener
        )
    }
}