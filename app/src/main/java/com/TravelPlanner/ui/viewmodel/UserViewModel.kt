package com.TravelPlanner.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.TravelPlanner.data.database.entities.User_Entities
import com.TravelPlanner.data.repository.User_Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: User_Repo
) : ViewModel() {

    private val _registerState = MutableStateFlow<Result<Boolean>?>(null)
    val registerState: StateFlow<Result<Boolean>?> = _registerState

    fun registerUser(
        user: User_Entities,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            val result = userRepository.registerUser(user)
            if (result.isSuccess) {
                _registerState.value = result
                onSuccess()
            } else {
                onError(result.exceptionOrNull() ?: Exception("Unknown error"))
            }
        }
    }

    fun clearRegisterState() {
        _registerState.value = null
    }
}
