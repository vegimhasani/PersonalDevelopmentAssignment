package com.vegimhasani.bux.detail

import com.vegimhasani.bux.detail.models.ProductsViewModel

sealed class DetailsState {

    data class ProductDetails(val viewModel: ProductsViewModel) : DetailsState()

}