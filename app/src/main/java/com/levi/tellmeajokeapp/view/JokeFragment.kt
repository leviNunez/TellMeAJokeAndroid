package com.levi.tellmeajokeapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.levi.tellmeajokeapp.databinding.FragmentJokeBinding
import com.levi.tellmeajokeapp.viewmodel.JokeViewModel
import com.levi.tellmeajokeapp.viewmodel.UiState
import com.levi.tellmeajokeapp.utils.*
import kotlinx.coroutines.*


class JokeFragment : Fragment() {
    companion object {
        const val PUNCHLINE_ANIMATION_DELAY_TIME = 300L
        fun newInstance() = JokeFragment()
    }

    private lateinit var binding: FragmentJokeBinding
    private val viewModel: JokeViewModel by viewModels { JokeViewModel.Factory }
    private var animationJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJokeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindClickListeners()
        bindState()
    }

    private fun bindClickListeners() {
        binding.apply {
            setupView.questionMarkButton.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    startSetupExitAnimation()
                    viewModel?.revealPunchline()
                }
            }
            punchlineView.nextButton.setOnClickListener {
                viewModel?.getJoke()
            }
            punchlineView.backButton.setOnClickListener {
                viewModel?.revealSetup()
            }
            errorView.retryButton.setOnClickListener {
                viewModel?.getJoke()
            }
        }
    }

    private suspend fun startSetupExitAnimation() {
        binding.apply {
            fadeViewOut(view = binding.setupView.setupContainer)
            scaleUp(view = binding.setupView.setupText)
                .awaitEnd()
        }
    }

    private fun bindState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { uiState ->
                        animationJob?.cancel()
                        binding.jokeHost.children.forEach { view ->
                            view.visibility = View.GONE
                        }
                        when (uiState) {
                            is UiState.Loading -> {
                                showProgressIndicator()
                            }

                            is UiState.ShowSetup -> {
                                showSetup(text = uiState.setup)
                            }

                            is UiState.ShowPunchline -> {
                                if (viewModel.punchlineAnimationFinished) {
                                    showPunchlineWithoutAnimation(uiState.punchline)
                                } else {
                                    showPunchline(text = uiState.punchline)
                                }
                            }

                            is UiState.Error -> {
                                showError()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showProgressIndicator() {
        binding.jokeProgressIndicatorView.visibility = View.VISIBLE
    }

    private fun showSetup(text: String) {
        binding.apply {
            setupView.setupText.text = text
            setupView.setupContainer.visibility = View.VISIBLE
            fadeViewIn(view = setupView.setupContainer)
        }
        startAnimationLoop()
    }

    private fun startAnimationLoop() {
        animationJob = viewLifecycleOwner.lifecycleScope.launch {
            val timeIntervalInMillis = 5000L
            while (true) {
                delay(timeIntervalInMillis)
                moveAndRotateView(view = binding.setupView.questionMarkButton)
            }
        }
    }

    private fun showPunchlineWithoutAnimation(text: String) {
        binding.apply {
            punchlineView.punchlineText.text = text
            punchlineView.punchlineContainer.visibility = View.VISIBLE
            punchlineView.laughImage.visibility = View.GONE
            punchlineView.buttonsContainer.visibility = View.VISIBLE
        }
    }

    private suspend fun showPunchline(text: String) {
        binding.apply {
            val container = punchlineView.punchlineContainer
            val punchlineText = punchlineView.punchlineText
            val laughImage = punchlineView.laughImage
            val buttonsContainer = punchlineView.buttonsContainer

            buttonsContainer.visibility = View.GONE
            container.visibility = View.VISIBLE
            laughImage.visibility = View.VISIBLE
            punchlineText.text = text
            laughImage.setImageResource(randomLaughImage())


            fadeViewIn(view = container)
            scaleDown(view = punchlineText)
            scaleViewUpAndDown(view = laughImage, context = requireContext())
                .awaitEnd()
            fadeViewOut(view = laughImage)

            delay(PUNCHLINE_ANIMATION_DELAY_TIME)
            viewModel?.punchlineAnimationDidFinish()

            laughImage.visibility = View.GONE
            buttonsContainer.visibility = View.VISIBLE

            fadeViewIn(view = buttonsContainer)
        }
    }

    private fun showError() {
        binding.errorView.errorContainer.visibility = View.VISIBLE
    }
}
