package com.vegimhasani.dott.details

import com.vegimhasani.dott.details.presentation.state.DetailsUiState
import com.vegimhasani.dott.details.presentation.viewmodel.DetailsViewModel
import com.vegimhasani.dott.details.usecases.GetSingleRestaurant
import com.vegimhasani.dott.map.domain.model.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DetailsViewModelTest {

    private val getSingleRestaurant: GetSingleRestaurant = mock()

    private val testDispatchers = TestCoroutineDispatcher()

    private val sut = DetailsViewModel(getSingleRestaurant)

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
    fun testGetRestaurantById() {
        runBlocking {
            val restaurant: Restaurant = mock()
            val id = "1"
            val name = "Name"
            val address = "Address"

            whenever(restaurant.id).thenReturn(id)
            whenever(restaurant.name).thenReturn(name)
            whenever(restaurant.address).thenReturn(address)
            val state = DetailsUiState.DisplayDetailsData(restaurant)
            whenever(getSingleRestaurant.getRestaurantWithId(id)).thenReturn(restaurant)
            sut.getRestaurant(id)
            assert(sut.uiState.value == state)
        }

    }
}