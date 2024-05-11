package com.example.offroad.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offroad.data.Profile
import com.example.offroad.data.ProfileRepository
import com.example.offroad.util.Async
import com.example.offroad.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * UiState for the profile screen.
 */
data class ProfileUiState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

/**
 * ViewModel for the profile screen.
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _taskAsync: MutableStateFlow<Async<Profile?>> = MutableStateFlow(Async.Loading)
    val uiState: StateFlow<ProfileUiState> = _taskAsync
        .map { taskAsync ->
            when (taskAsync) {
                Async.Loading -> ProfileUiState(isLoading = true)

                is Async.Error -> ProfileUiState(
                    userMessage = taskAsync.errorMessage
                )

                is Async.Success -> ProfileUiState(
                    profile = taskAsync.data,
                    isLoading = false
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ProfileUiState(isLoading = true)
        )

    fun loadProfile() {
        _taskAsync.value = Async.Loading
        viewModelScope.launch {
            try {
                val profile = profileRepository.getProfile()
                _taskAsync.value = Async.Success(profile)
            } catch (e: Exception) {
                _taskAsync.value =
                    Async.Error(
                        e.message ?: "Unknown error"
                    )
            }
        }
    }

    fun updateProfile(pui: ProfileUpdateInput) {
        _taskAsync.value = Async.Loading
        viewModelScope.launch {
            try {
                profileRepository.updateProfile(
                    pui.imageUrl,
                    pui.fullName,
                    pui.email,
                    pui.birthdayDate,
                    pui.phoneNumber,
                    pui.subscribed
                )
            } catch (e: Exception) {
                _taskAsync.value = Async.Error(e.message ?: "Failed to update profile")
            }

            loadProfile()
        }
    }
}


data class ProfileUpdateInput(
    val imageUrl: String,
    val fullName: String,
    val email: String,
    val birthdayDate: String,
    val phoneNumber: String,
    val subscribed: Boolean
)