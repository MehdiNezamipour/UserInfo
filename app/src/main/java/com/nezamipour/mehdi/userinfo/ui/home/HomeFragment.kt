package com.nezamipour.mehdi.userinfo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.databinding.FragmentHomeBinding
import com.nezamipour.mehdi.userinfo.paging.UserAdapter
import com.nezamipour.mehdi.userinfo.paging.UserClickListener
import com.nezamipour.mehdi.userinfo.paging.UserLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var pagingAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagingAdapter.userClickListener = object : UserClickListener {
            override fun onUserClicked(user: User) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                        user
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        loadData()
    }


    private fun initUi() {
        initAdapter()
        binding.homeRecyclerView.setHasFixedSize(true)
    }

    @ExperimentalPagingApi
    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagingUser().collectLatest {
                pagingAdapter.submitData(it)
            }
        }


    }

    private fun initAdapter() {
        binding.swipeRefresher.setOnRefreshListener {
            pagingAdapter.refresh()
        }
        /* viewLifecycleOwner.lifecycleScope.launch {
             pagingAdapter.loadStateFlow.distinctUntilChangedBy {
                 it.refresh
             }
                 .filter {
                     it.refresh is LoadState.NotLoading
                 }
                 .collect {
                     binding.homeRecyclerView.scrollToPosition(0)
                 }
         }*/
        binding.homeRecyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = UserLoadStateAdapter { pagingAdapter.retry() },
            footer = UserLoadStateAdapter { pagingAdapter.retry() }
        )

        pagingAdapter.addLoadStateListener { loadState ->
            val refreshState = loadState.mediator?.refresh
            binding.swipeRefresher.isRefreshing = refreshState is LoadState.Loading
            handleError(loadState)
        }

    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.source.refresh as? LoadState.Error
            ?: loadState.mediator?.refresh as? LoadState.Error
            ?: loadState.mediator?.append as? LoadState.Error
            ?: loadState.mediator?.prepend as? LoadState.Error

        errorState?.let {
            Snackbar.make(requireView(), "Connection lost", Snackbar.LENGTH_SHORT).show()
        }
    }

}