package com.vegimhasani.bux.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vegimhasani.bux.databinding.DetailFragmentBinding
import com.vegimhasani.bux.main.MainState
import dagger.hilt.android.AndroidEntryPoint

private const val PRODUCT_ID = "PRODUCT_ID"

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val productId: String by lazy {
        checkNotNull(arguments?.getString(PRODUCT_ID)) { "Product ID must be provided" }
    }

    companion object {
        fun newInstance(productId: String) = DetailFragment().apply {
            arguments = Bundle().apply { putString(PRODUCT_ID, productId) }
        }
    }

    private val viewModel by viewModels<DetailViewModel>()


    private lateinit var binding: DetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is DetailsState.PriceChange -> {
                    updateData()
                }
            }
        }
    }

    private fun updateData() {
        Toast.makeText(requireContext(), "Price updated", Toast.LENGTH_SHORT).show()
    }
}
