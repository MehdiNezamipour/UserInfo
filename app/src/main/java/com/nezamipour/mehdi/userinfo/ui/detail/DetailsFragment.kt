package com.nezamipour.mehdi.userinfo.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nezamipour.mehdi.userinfo.R
import com.nezamipour.mehdi.userinfo.data.model.User
import com.nezamipour.mehdi.userinfo.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var user: User
    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.let { DetailsFragmentArgs.fromBundle(it).user }!!
        viewModel.user.value = user
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        setListeners()
    }


    private fun setListeners() {
        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageViewBookmark.setOnClickListener {
            val bookmarkState = viewModel.getBookmarkState()
            viewModel.insertOrReplaceBookmark(user.id, !bookmarkState)
            changeUi(!bookmarkState)
        }

    }

    private fun initUi() {
        val bookmarkState = viewModel.getBookmarkState()
        changeUi(bookmarkState)

        binding.imageViewUserImage.apply {
            Glide.with(this)
                .load(user.avatar)
                .into(this)
        }
        val name = resources.getString(R.string.user_full_name, user.first_name, user.last_name)
        binding.textViewUserName.text = name
        binding.textViewFullName.text = name


    }

    private fun changeUi(bookmark: Boolean) {
        if (bookmark) {
            binding.imageViewBookmark.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_fill
                )
            )
        } else
            binding.imageViewBookmark.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark
                )
            )
    }


}