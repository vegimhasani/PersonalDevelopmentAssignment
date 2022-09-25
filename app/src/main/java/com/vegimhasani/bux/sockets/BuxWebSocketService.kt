package com.vegimhasani.bux.sockets

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import com.vegimhasani.bux.sockets.models.Subscribe
import kotlinx.coroutines.flow.Flow

interface BuxWebSocketService {

    @Receive
    fun observeWebSocket(): Flow<WebSocket.Event>

    @Send
    fun sendSubscribe(subscribe: Subscribe)
}
