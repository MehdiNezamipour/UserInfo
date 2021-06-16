package com.nezamipour.mehdi.userinfo.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.nezamipour.mehdi.userinfo.R
import com.nezamipour.mehdi.userinfo.databinding.LoadStateItemBinding

class UserLoadStateViewHolder(
    private val binding: LoadStateItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.buttonRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.textViewError.text = binding.root.context.getString(R.string.network_error)
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.buttonRetry.isVisible = loadState !is LoadState.Loading
        binding.textViewError.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): UserLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_item, parent, false)
            val binding = LoadStateItemBinding.bind(view)
            return UserLoadStateViewHolder(binding, retry)
        }
    }
}