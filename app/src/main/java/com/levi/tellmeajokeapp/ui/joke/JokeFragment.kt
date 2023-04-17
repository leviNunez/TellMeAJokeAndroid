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
import com.google.android.material.snackbar.Snackbar
import com.levi.tellmeajokeapp.R
import com.levi.tellmeajokeapp.databinding.FragmentJokeBinding
import com.levi.tellmeajokeapp.util.*
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_PUNCHLINE_VISIBLE_KEY, isPunchlineVisible)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJokeBinding.inflate(layoutInflater, container, false)

        savedInstanceState?.getBoolean(IS_PUNCHLINE_VISIBLE_KEY)?.let { isVisible ->
            if (isVisible) hideSetupAndShowPunchline()
            isPunchlineVisible = isVisible
        }

        bindClickListeners()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun hideSetupAndShowPunchline() {
        binding.apply {
            setupText.visibility = View.GONE
            questionMarkButton.visibility = View.GONE
            punchlineText.visibility = View.VISIBLE
            controlButtonsContainer.visibility = View.VISIBLE
        }
    }

    private fun bindClickListeners() {
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
                        uiState.errorMessage?.let { message ->
                            Snackbar.make(
                                binding.mainLayout,
                                getString(R.string.snackbar_error_text, message),
                                Snackbar.LENGTH_LONG
                            ).setTextMaxLines(1)
                                .show()
                            viewModel.snackbarShown()
                        }
                    }
                }
                launch {
                    viewModel.uiAction.collect { uiAction ->
                        when (uiAction) {
                            UiAction.Next -> {
                                isPunchlineVisible = false
                                hidePunchlineAndShowSetup()
                                startSetupAnimation()
                            }
                            UiAction.RevealPunchline -> {
                                isPunchlineVisible = true
                                startPunchlineAnimation()
                            }
                            UiAction.Back -> {
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
        cancelAnimationLoop()
        animationJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(5000)
                binding.apply {
                    translateAndRotateView(
                        view = buttonsContainer,
                        onStart = { questionMarkButton.isEnabled = false },
                        onEnd = { questionMarkButton.isEnabled = true }
                    )
                }
            }
        }
    }

    private fun cancelAnimationLoop() {
        animationJob?.cancel()
    }

    private fun hidePunchlineAndShowSetup() {
        binding.apply {
            punchlineText.visibility = View.GONE
            controlButtonsContainer.visibility = View.GONE
            setupText.visibility = View.VISIBLE
            questionMarkButton.visibility = View.VISIBLE
        }
    }

    private fun startSetupAnimation() {
        binding.apply {
            fadeViewIn(
                view = mainLayout,
                onStart = { questionMarkButton.isEnabled = false },
                onEnd = { questionMarkButton.isEnabled = true })
        }
    }

    private fun startPunchlineAnimation() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                fadeAndScaleView(
                    view = jokeContainer,
                    onRepeat = {
                        setupText.visibility = View.GONE
                        punchlineText.visibility = View.VISIBLE
                    }, onEnd = {
                        setupText.visibility = View.GONE
                        punchlineText.visibility = View.VISIBLE
                        jokeContainer.alpha = 1f
                    })

                fadeViewOut(
                    view = questionMarkButton,
                    onStart = { questionMarkButton.isEnabled = false },
                    onEnd = {
                        questionMarkButton.apply {
                            visibility = View.GONE
                            isEnabled = true
                            alpha = 1f
                        }
                    }).also { it.awaitEnd() }

                delay(200L)

                fadeViewIn(view = laughImage, onStart = { laughImage.visibility = View.VISIBLE })

                scaleViewUpAndDown(view = laughImage).also { it.awaitEnd() }

                fadeViewOut(
                    view = laughImage,
                    onEnd = { laughImage.visibility = View.GONE })

                delay(100L)

                fadeViewIn(
                    view = controlButtonsContainer,
                    onStart = { controlButtonsContainer.visibility = View.VISIBLE })
            }
        }
    }
}
