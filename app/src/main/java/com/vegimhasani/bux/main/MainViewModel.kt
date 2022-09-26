package com.vegimhasani.bux.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    init {
        _state.postValue(MainState.DisplayData)
    }
}
