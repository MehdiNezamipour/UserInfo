package com.nezamipour.mehdi.userinfo.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.nezamipour.mehdi.userinfo.R
import com.nezamipour.mehdi.userinfo.databinding.LoadStateItemBinding


// Adapter that displays a loading spinner when
// state = LoadState.Loading, and an error message and retry
// button when state is LoadState.Error.
class UserLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = LoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class LoadStateViewHolder(
    parent: ViewGroup,
    append: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.load_state_item, parent, false)
) {
    private val binding = LoadStateItemBinding.bind(itemView)
    private val progressBar: ProgressBar = binding.progressBar

    private val buttonLoadMore: MaterialButton = binding.buttonLoadMore
        .also {
            it.setOnClickListener { append() }
        }

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        if (loadState.endOfPaginationReached) {
            buttonLoadMore.visibility = View.VISIBLE
        }

    }
}