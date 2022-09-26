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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: BuxApiService,
    private val webSocketService: BuxWebSocketService,
    savedStateHandle: SavedStateHandle
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
                    // Display data of the details view
                }
            }
        }
    }

    private fun initWebSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            webSocketService.observeWebSocket().collect {
                onReceiveResponseConnection(it)
            }
        }
    }

    private fun onReceiveResponseConnection(response: WebSocket.Event) {
        when (response) {
            is WebSocket.Event.OnConnectionOpened<*> -> {
                _state.postValue(DetailsState.ConnectionState("Connection opened"))
            }
            is WebSocket.Event.OnConnectionClosed -> {
                _state.postValue(DetailsState.ConnectionState(response.shutdownReason.reason))
            }
            is WebSocket.Event.OnConnectionClosing -> {
                _state.postValue(DetailsState.ConnectionState(response.shutdownReason.reason))
            }
            is WebSocket.Event.OnConnectionFailed -> {
                _state.postValue(DetailsState.ConnectionState("Connection failed"))
            }
            is WebSocket.Event.OnMessageReceived -> {
                handleOnMessageReceived(response.message)
            }
        }
    }

    private fun handleOnMessageReceived(message: Message) {
        when (message) {
            is Message.Text -> {
                handleTextMessage(message)
            }
            is Message.Bytes -> {
                _state.postValue(DetailsState.ConnectionState("unexpected message"))
            }
        }
    }

    private fun handleTextMessage(message: Message.Text) {
        val webSocketResponseBody = Gson().fromJson(message.value, WebSocketResponseBody::class.java)
        when (webSocketResponseBody.t) {
            "connect.connected" -> {
                subscribeToWebSocket()
            }
            "trading.quote" -> {
                // Update the real time price
            }
            else -> {
                _state.postValue(DetailsState.ConnectionState("Unexpected event"))
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
