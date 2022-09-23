package com.vegimhasani.bux.detail

import androidx.lifecycle.*
import com.tinder.scarlet.WebSocket
import com.vegimhasani.bux.sockets.BuxApiService
import com.vegimhasani.bux.sockets.BuxWebSocketService
import com.vegimhasani.bux.sockets.models.Subscribe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PRODUCT_ID = "PRODUCT_ID"

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: BuxApiService,
    private val webSocketService: BuxWebSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableLiveData<DetailsState>()
    val state: LiveData<DetailsState> = _state

    init {
        getProductDetails()
    }

    private fun getProductDetails() {
        viewModelScope.launch {
            savedStateHandle.get<String>(PRODUCT_ID)?.let {
                val response = apiService.getProductDetails(it)
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    productsResponse?.securityId?.let { productId -> listenToWebSocket(productId) }
                }
            }
        }
    }

    private fun listenToWebSocket(productId: String) {
        webSocketService.observeWebSocket()
            .flowOn(Dispatchers.IO)
            .onEach { event ->
                if (event is WebSocket.Event.OnConnectionOpened<*>) {
                    webSocketService.sendSubscribe(
                        Subscribe(
                            subscribeTo = listOf(productId),
                            unsubscribeFrom = emptyList()
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
        webSocketService.observePriceChange()
            .flowOn(Dispatchers.IO)
            .onEach {
                _state.postValue(DetailsState.PriceChange)
            }
            .launchIn(viewModelScope)
    }
}
