package com.vegimhasani.bux.detail

import androidx.lifecycle.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import com.vegimhasani.bux.detail.models.ProductsResponse
import com.vegimhasani.bux.detail.models.ProductsViewModel
import com.vegimhasani.bux.sockets.BuxApiService
import com.vegimhasani.bux.sockets.BuxWebSocketService
import com.vegimhasani.bux.sockets.models.Subscribe
import com.vegimhasani.bux.sockets.models.WebSocketResponseBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: BuxApiService,
    private val webSocketService: BuxWebSocketService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _detailsState = MutableLiveData<DetailsState>()
    val detailsState: LiveData<DetailsState> = _detailsState
    private val _connectionState = MutableLiveData<ConnectionState>()
    val connectionState: LiveData<ConnectionState> = _connectionState
    private val _priceUpdateState = MutableLiveData<String>()
    val priceUpdateState: LiveData<String> = _priceUpdateState
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
                    val productsResponse = response.body()
                    productsResponse?.let {
                        // Display data of the details view
                        _detailsState.postValue(DetailsState.ProductDetails(toProductsViewModel(it)))
                    }
                }
            }
        }
    }

    private fun toProductsViewModel(response: ProductsResponse): ProductsViewModel {
        val currentPrice = BigDecimal(response.currentPrice.amount)
        val closingPrice = BigDecimal(response.closingPrice.amount)
        return ProductsViewModel(
            displayName = "Display name: ${response.displayName}",
            currentPriceFormatted = "Current price: ${response.currentPrice.amount} ${response.currentPrice.currency}",
            closingPriceFormatted = "Closing price: ${response.closingPrice.amount} ${response.closingPrice.currency}",
            percentageDifference = "Percentage difference : ${percentageBetween(currentPrice, closingPrice)} %"
        )
    }

    private fun percentageBetween(currentPrice: BigDecimal, closingPrice: BigDecimal): BigDecimal {
        return currentPrice.subtract(closingPrice).divide(BigDecimal(100))
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
                _connectionState.postValue(ConnectionState("Connection opened"))
            }
            is WebSocket.Event.OnConnectionClosed -> {
                _connectionState.postValue(ConnectionState(response.shutdownReason.reason))
            }
            is WebSocket.Event.OnConnectionClosing -> {
                _connectionState.postValue(ConnectionState(response.shutdownReason.reason))
            }
            is WebSocket.Event.OnConnectionFailed -> {
                _connectionState.postValue(ConnectionState("Connection failed"))
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
                _connectionState.postValue(ConnectionState("unexpected message"))
            }
        }
    }

    private fun handleTextMessage(message: Message.Text) {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<WebSocketResponseBody> = moshi.adapter(WebSocketResponseBody::class.java)
        val webSocketResponseBody = jsonAdapter.fromJson(message.value);
        when (webSocketResponseBody?.t) {
            "connect.connected" -> {
                subscribeToWebSocket()
            }
            "trading.quote" -> {
                // Publish the real time price
                publishRealTimePrice(webSocketResponseBody.body.currentPrice)

            }
        }
    }

    private fun publishRealTimePrice(currentPrice: String?) {
        val formattedRealTimePrice = "Real time price: $currentPrice"
        _priceUpdateState.postValue(formattedRealTimePrice)
    }

    private fun subscribeToWebSocket() {
        webSocketService.sendSubscribe(
            Subscribe(
                subscribeTo = listOf("trading.product.$productId")
            )
        )
    }
}
