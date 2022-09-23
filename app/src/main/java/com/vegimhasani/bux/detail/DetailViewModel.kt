package com.vegimhasani.bux.detail

import androidx.lifecycle.*
import com.google.gson.Gson
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import com.vegimhasani.bux.sockets.BuxApiService
import com.vegimhasani.bux.sockets.BuxWebSocketService
import com.vegimhasani.bux.sockets.models.Subscribe
import com.vegimhasani.bux.sockets.models.WebSocketResponseBody
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
        listenToWebSocket()
    }

    private fun getProductDetails() {
        viewModelScope.launch {
            savedStateHandle.get<String>(PRODUCT_ID)?.let {
                val response = apiService.getProductDetails(it)
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                }
            }
        }
    }

    private fun listenToWebSocket() {
        webSocketService.observeWebSocket()
            .flowOn(Dispatchers.IO)
            .onEach { event ->
                if (event is WebSocket.Event.OnMessageReceived) {
                    when (event.message) {
                        is Message.Text -> {
                            val webSocketResponseBody = Gson().fromJson((event.message as Message.Text).value, WebSocketResponseBody::class.java)
                            if (webSocketResponseBody.t == "connect.connected"){
                                 subscribeToWebSocket()
                            }
                        }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun subscribeToWebSocket() {
        savedStateHandle.get<String>(PRODUCT_ID)?.let {
            webSocketService.sendSubscribe(
                Subscribe(
                    subscribeTo = listOf("trading.product.{$it}")
                )
            )
        }
    }

}
