package com.vegimhasani.dott.details.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.vegimhasani.dott.R
import com.vegimhasani.dott.databinding.FragmentDetailsBinding
import com.vegimhasani.dott.details.presentation.state.DetailsUiState
import com.vegimhasani.dott.details.presentation.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var fragmentDetailsBinding: FragmentDetailsBinding

    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDetailsBinding = FragmentDetailsBinding.bind(view)
        init()
    }

    private fun init() {
        // Start a coroutine in the lifecycle scope
        detailsViewModel.getRestaurant(args.restaurantId)
        lifecycleScope.launch {
            detailsViewModel.uiState.collect {
                when (it) {
                    is DetailsUiState.Loading -> {
                        Toast.makeText(requireContext(), R.string.loading_message, Toast.LENGTH_SHORT).show()
                    }
                    is DetailsUiState.DisplayDetailsData -> {
                        updateUI(it.restaurant.name, it.restaurant.address)
                    }
                }
            }
        }
    }

    private fun updateUI(name: String, address: String?) {
        fragmentDetailsBinding.restaurantName.text = name
        fragmentDetailsBinding.restaurantAddress.text = address ?: resources.getString(R.string.details_address_not_available_message)
    }
}