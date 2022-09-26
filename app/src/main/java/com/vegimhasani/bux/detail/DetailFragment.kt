package com.vegimhasani.bux.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vegimhasani.bux.databinding.DetailFragmentBinding
import com.vegimhasani.bux.detail.models.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

const val PRODUCT_ID = "PRODUCT_ID"

@AndroidEntryPoint
class DetailFragment : Fragment() {

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
                is DetailsState.ConnectionState -> {
                    displayMessage(it.message)
                }
                is DetailsState.ProductDetails -> displayProductDetails(it.viewModel)
            }
        }
        viewModel.priceUpdateState.observe(viewLifecycleOwner) {
            binding.realTimeUpdatedPrice.text = it
        }
    }

    private fun displayProductDetails(viewModel: ProductsViewModel) {
        binding.productName.text = viewModel.displayName
        binding.productCurrentPrice.text = viewModel.currentPriceFormatted
        binding.productClosingPrice.text = viewModel.closingPriceFormatted
        binding.productPercentageDifference.text = viewModel.percentageDifference
    }

    private fun displayMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
