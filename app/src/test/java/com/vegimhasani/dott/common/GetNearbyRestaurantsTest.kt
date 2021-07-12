package com.vegimhasani.dott.common

import com.vegimhasani.dott.common.data.repository.FoursquareRepository
import com.vegimhasani.dott.common.data.service.model.request.RequestModel
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponse
import com.vegimhasani.dott.common.data.service.model.response.FoursquareResponseState
import com.vegimhasani.dott.common.data.service.model.response.Location
import com.vegimhasani.dott.common.data.service.model.response.Venue
import com.vegimhasani.dott.map.domain.model.LatitudeLongitude
import com.vegimhasani.dott.map.domain.model.UserNearbyRestaurantsState
import com.vegimhasani.dott.map.domain.usecases.GetNearbyRestaurants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetNearbyRestaurantsTest {

    private val repository: FoursquareRepository = mock()

    private val testDispatchers = TestCoroutineDispatcher()

    private val sut = GetNearbyRestaurants(repository, testDispatchers)

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
    fun testSuccessResponse() {
        runBlocking {
            val requestModel: RequestModel = mock()
            val latitudeLongitude: LatitudeLongitude = mock()
            val successState: FoursquareResponseState.Success = mock()
            val foursquareResponse: FoursquareResponse = mock()
            val venue: Venue = mock()
            val location: Location = mock()
            whenever(location.lat).thenReturn(59.00)
            whenever(location.address).thenReturn("address")
            whenever(location.lng).thenReturn(9.00)
            whenever(venue.id).thenReturn("id")
            whenever(venue.name).thenReturn("name")
            whenever(venue.location).thenReturn(location)
            whenever(venue.id).thenReturn("id")
            whenever(venue.id).thenReturn("id")

            val venuesList: MutableList<Venue> = mutableListOf(venue)
            val response: com.vegimhasani.dott.common.data.service.model.response.Response = mock()
            whenever(response.venues).thenReturn(venuesList)
            whenever(foursquareResponse.response).thenReturn(response)
            whenever(successState.response).thenReturn(foursquareResponse)
            whenever(latitudeLongitude.lat).thenReturn(50.00)
            whenever(latitudeLongitude.lat).thenReturn(4.23)
            whenever(repository.getAllRestaurants(requestModel)).thenReturn(successState)
            val sut = GetNearbyRestaurants(repository, testDispatchers)
            val state = sut.getNearbyRestaurants(latitudeLongitude, requestModel)
            Assert.assertTrue(state is UserNearbyRestaurantsState.Success)
        }

    }

    @Test
    fun testFailedResponse() {
        runBlocking {
            val requestModel: RequestModel = mock()
            val latitudeLongitude: LatitudeLongitude = mock()
            val errorState: FoursquareResponseState.Error = mock()
            whenever(repository.getAllRestaurants(requestModel)).thenReturn(errorState)
            val state = sut.getNearbyRestaurants(latitudeLongitude, requestModel)
            Assert.assertTrue(state is UserNearbyRestaurantsState.Failed)
        }
    }
}