package com.levi.tellmeajokeapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.levi.tellmeajokeapp.databinding.FragmentJokeBinding

class JokeFragment : Fragment() {

    companion object {
        fun newInstance() = JokeFragment()
    }

    private val viewModel: JokeViewModel by viewModels { JokeViewModel.Factory }
    private lateinit var binding: FragmentJokeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJokeBinding.inflate(layoutInflater, container, false).apply {
            viewmodel = viewmodel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

}