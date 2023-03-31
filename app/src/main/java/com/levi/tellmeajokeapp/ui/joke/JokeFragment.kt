package com.levi.tellmeajokeapp.ui.joke

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.levi.tellmeajokeapp.databinding.FragmentJokeBinding
import com.levi.tellmeajokeapp.util.fadeViewIn
import com.levi.tellmeajokeapp.util.fadeViewOut
import com.levi.tellmeajokeapp.util.scaleViewUpAndDown
import com.levi.tellmeajokeapp.util.translateAndRotateView
import kotlinx.coroutines.*


class JokeFragment : Fragment() {

    companion object {
        private const val IS_PUNCHLINE_VISIBLE_KEY = "isPunchlineVisible"
        fun newInstance() = JokeFragment()
    }

    private lateinit var binding: FragmentJokeBinding
    private val viewModel: JokeViewModel by viewModels { JokeViewModel.Factory }
    private var isPunchlineVisible = false
    private var animationJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJokeBinding.inflate(layoutInflater, container, false)

        bindListeners()

        isPunchlineVisible = savedInstanceState?.getBoolean(IS_PUNCHLINE_VISIBLE_KEY) ?: false

        if (isPunchlineVisible) hideSetupAndShowPunchline()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun bindListeners() {
        binding.apply {
            questionMarkButton.setOnClickListener {
                viewModel?.revealPunchline()
            }

            nextButton.setOnClickListener {
                viewModel?.next()
            }

            backButton.setOnClickListener {
                viewModel?.back()
            }

            errorLayoutContainer.retryButton.setOnClickListener {
                viewModel?.next()
            }
        }
    }

    private fun hideSetupAndShowPunchline() {
        binding.apply {
            setupText.visibility = View.GONE
            questionMarkButton.visibility = View.GONE
            punchlineText.visibility = View.VISIBLE
            backNextButtonsRl.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { uiState ->
                        when {
                            uiState.shouldPlayAnimationLoop -> startAnimationLoop()
                            else -> cancelAnimationLoop()
                        }
                    }
                }
                launch {
                    viewModel.uiEvent.collect { uiAction ->
                        when (uiAction) {
                            UiActions.Next -> {
                                isPunchlineVisible = false
                                startSetupAnimation()
                            }
                            UiActions.RevealPunchline -> {
                                isPunchlineVisible = true
                                startPunchlineAnimation()
                            }
                            UiActions.Back -> {
                                isPunchlineVisible = false
                                hidePunchlineAndShowSetup()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startAnimationLoop() {
        val animationInterval = 5000L
        cancelAnimationLoop()
        animationJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(animationInterval)
                translateAndRotateView(
                    view = binding.controlButtonsContainer,
                    animDuration = 400
                )
            }
        }
    }

    private fun cancelAnimationLoop() {
        animationJob?.cancel()
    }

    private fun startSetupAnimation() {
        binding.apply {
            punchlineText.visibility = View.GONE
            backNextButtonsRl.visibility = View.GONE
            fadeViewIn(view = setupText, animDuration = 1000)
            fadeViewIn(view = questionMarkButton, animDuration = 1000)
        }
    }

    private fun startPunchlineAnimation() {
        binding.apply {
            scaleViewUpAndDown(view = jokeContainer, animDuration = 600L)
            fadeViewOut(view = setupText)
            fadeViewOut(view = questionMarkButton)
            fadeViewIn(view = punchlineText, animDuration = 1000L, animDelay = 500L)
            fadeViewIn(view = laughImage, animDelay = 700L)
            scaleViewUpAndDown(view = laughImage, repeat = 3, animDelay = 700L)
            fadeViewOut(view = laughImage, animDelay = 2300L)
            fadeViewIn(view = backNextButtonsRl, animDelay = 2700L)
        }
    }

    private fun hidePunchlineAndShowSetup() {
        binding.apply {
            punchlineText.visibility = View.GONE
            backNextButtonsRl.visibility = View.GONE
            setupText.visibility = View.VISIBLE
            questionMarkButton.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_PUNCHLINE_VISIBLE_KEY, isPunchlineVisible)
    }

}