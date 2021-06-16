package com.nezamipour.mehdi.userinfo.paging

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nezamipour.mehdi.userinfo.R
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.databinding.ListItemLayoutBinding

class UserViewHolder(
    private val binding: ListItemLayoutBinding,
    private val userClickListener: UserClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    var user: User? = null

    fun bind(user: User) {
        this.user = user
        binding.textViewName.text = binding.root.context.resources.getString(
            R.string.user_full_name,
            user.first_name,
            user.last_name
        )
        binding.textViewEmail.text = user.email

        binding.imageViewAvatar.apply {
            Glide.with(binding.root)
                .load(user.avatar)
                .apply(
                    RequestOptions.circleCropTransform()
                )
                .into(this)
        }
        binding.root.setOnClickListener {
            userClickListener.onUserClicked(user)
        }

    }

}