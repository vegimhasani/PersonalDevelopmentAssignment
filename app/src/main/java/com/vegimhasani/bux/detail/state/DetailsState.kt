package com.vegimhasani.bux.detail.state

import com.vegimhasani.bux.detail.models.ProductsViewModel

sealed class DetailsState {

    data class ProductDetails(val viewModel: ProductsViewModel) : DetailsState()

}