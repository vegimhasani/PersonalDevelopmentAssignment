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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: BuxApiService,
    private val webSocketService: BuxWebSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableLiveData<DetailsState>()
    val state: LiveData<DetailsState> = _state
    private val productId = savedStateHandle.get<String>(PRODUCT_ID)

    init {
        getProductDetails()
        initWebSocket()
    }

    private fun getProductDetails() {
        viewModelScope.launch {
            productId?.let {
                val response = apiService.getProductDetails(productId)
                if (response.isSuccessful) {
                    subscribeToWebSocket()
                   // Display data of the details
                }
            }
        }
    }

    private fun initWebSocket() {
        viewModelScope.launch {
            webSocketService.observeWebSocket().collect {
                onReceiveResponseConnection(it)
            }
        }
    }

    private fun onReceiveResponseConnection(response: WebSocket.Event) {
        when (response) {
            is WebSocket.Event.OnConnectionOpened<*> -> {
                _state.postValue(DetailsState.ConnectionState("connection opened"))
            }
            is WebSocket.Event.OnConnectionClosed -> {
                _state.postValue(DetailsState.ConnectionState("connection closed"))
            }
            is WebSocket.Event.OnConnectionClosing -> {
                _state.postValue(DetailsState.ConnectionState("closing connection.."))
            }
            is WebSocket.Event.OnConnectionFailed -> {
                _state.postValue(DetailsState.ConnectionState("connection failed"))
            }
            is WebSocket.Event.OnMessageReceived -> {
                handleOnMessageReceived(response.message)
            }
        }
    }

    private fun handleOnMessageReceived(message: Message) {
        when (message) {
            is Message.Text -> {
                val webSocketResponseBody = Gson().fromJson(message.value, WebSocketResponseBody::class.java)
                if (webSocketResponseBody.t == "connect.connected") {
                    // Update the price value"
                }
            }
            is Message.Bytes -> {
                _state.postValue(DetailsState.ConnectionState("unexpected message"))
            }
        }
    }

    private fun subscribeToWebSocket() {
        webSocketService.sendSubscribe(
            Subscribe(
                subscribeTo = listOf("trading.product.{$productId}")
            )
        )
    }
}
