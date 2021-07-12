package com.vegimhasani.dott.common

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.common.data.service.FoursquareService
import com.vegimhasani.dott.common.data.service.model.request.LatitudeLongitudeRequest
import com.vegimhasani.dott.common.data.service.model.request.RequestModel
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponse
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class FoursquareRepositoryTest {

    private val service: FoursquareService = mock()

    private val testDispatchers = TestCoroutineDispatcher()

    private val sut = FoursquareRepository(service, testDispatchers)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatchers)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatchers.cleanupTestCoroutines()
    }

    @Test
    fun testErrorResponse() {
        runBlocking {
            val clientId = "ID"
            val clientSecret = "secretPassword"
            val version = "v2"
            val categoryId = "233"
            val latLngRequest: LatitudeLongitudeRequest = mock()
            val requestModel: RequestModel = mock()
            val retrofitResponse: Response<FoursquareResponse> = mock()
            whenever(requestModel.clientID).thenReturn(clientId)
            whenever(requestModel.clientSecret).thenReturn(clientSecret)
            whenever(requestModel.latlng).thenReturn(latLngRequest)
            whenever(requestModel.version).thenReturn(version)
            whenever(requestModel.categoryId).thenReturn(categoryId)
            whenever(retrofitResponse.isSuccessful).thenReturn(false)
            whenever(retrofitResponse.body()).thenReturn(null)
            whenever(
                service.getRestaurantsNearby(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            ).thenReturn(retrofitResponse)

            val state = sut.getAllRestaurants(requestModel)

            assert(state is FoursquareResponseState.Error)
        }

    }
}