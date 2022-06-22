package com.vegimhasani.lightyear.details.viewmodel

import com.vegimhasani.lightyear.details.presentation.viewmodel.LightyearViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

class LightyearViewModelTest {

    private val getSingleRestaurant: GetSingleRestaurant = mock()

    private val testDispatchers = TestCoroutineDispatcher()

    private val sut = LightyearViewModel(getSingleRestaurant)

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


    }
}