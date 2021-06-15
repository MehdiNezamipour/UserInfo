package com.nezamipour.mehdi.userinfo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import com.nezamipour.mehdi.userinfo.databinding.FragmentHomeBinding
import com.nezamipour.mehdi.userinfo.paging.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding


    @Inject
    lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding.homeRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.homeRecyclerView.setHasFixedSize(true)


        /*      viewLifecycleOwner.lifecycleScope.launch {
                  adapter.loadStateFlow.collectLatest { loadStates ->
                      if (loadStates.source.append.endOfPaginationReached) {
                          binding.buttonLoadMore.visibility = View.VISIBLE
                      }
                  }
              }*/

    }

    @ExperimentalPagingApi
    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagingUser().collectLatest {
                adapter.submitData(it)
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}