package com.example.farmgate.presentation.customer.rating


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.farmgate.core.common.Resource
import com.example.farmgate.data.repository.RatingRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateRatingViewModel(
    private val ratingRepository: RatingRepository,
    private val orderId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateRatingUiState())
    val uiState: StateFlow<CreateRatingUiState> = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<String>()
    val navigation: SharedFlow<String> = _navigation.asSharedFlow()

    fun onScoreChanged(value: Int) {
        _uiState.value = _uiState.value.copy(
            score = value.coerceIn(1, 5),
            errorMessage = null
        )
    }

    fun onCommentChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            comment = value,
            errorMessage = null
        )
    }

    fun submit() {
        val state = _uiState.value

        if (state.score !in 1..5) {
            _uiState.value = state.copy(errorMessage = "Score must be between 1 and 5.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSubmitting = true, errorMessage = null, successMessage = null)

            when (
                val result = ratingRepository.createRating(
                    orderId = orderId,
                    score = state.score,
                    comment = state.comment.trim().ifBlank { null }
                )
            ) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        successMessage = "Rating submitted successfully."
                    )
                    _navigation.emit("back")
                }

                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        errorMessage = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isSubmitting = true)
                }
            }
        }
    }

    class Factory(
        private val ratingRepository: RatingRepository,
        private val orderId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateRatingViewModel(ratingRepository, orderId) as T
        }
    }
}