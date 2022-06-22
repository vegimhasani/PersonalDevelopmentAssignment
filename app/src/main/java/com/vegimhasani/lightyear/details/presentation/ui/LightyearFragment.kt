package com.vegimhasani.lightyear.details.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.vegimhasani.lightyear.R
import com.vegimhasani.lightyear.databinding.FragmentLightyearBinding
import com.vegimhasani.lightyear.details.presentation.state.DetailsUiState
import com.vegimhasani.lightyear.details.presentation.viewmodel.LightyearViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LightyearFragment : Fragment() {

    private lateinit var fragmentLightyearBinding: FragmentLightyearBinding

    private val lightyearViewModel: LightyearViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lightyear, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLightyearBinding = FragmentLightyearBinding.bind(view)
        init()
    }

    private fun init() {
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            lightyearViewModel.uiState.collect {
                when (it) {
                    is DetailsUiState.Loading -> {
                        Toast.makeText(requireContext(), R.string.loading_message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}