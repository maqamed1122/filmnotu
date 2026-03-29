package com.filmnot.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmnot.data.repository.AuthRepository
import com.filmnot.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: FirebaseUser? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val authState: StateFlow<FirebaseUser?> = authRepository.authState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), authRepository.currentUser)

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = authRepository.signInWithEmail(email, password)
            if (result.isSuccess) {
                // Sync watchlist from cloud after login
                try { firestoreRepository.syncFromCloud() } catch (_: Exception) {}
                _uiState.value = AuthUiState(user = result.getOrNull())
            } else {
                _uiState.value = AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            val result = authRepository.registerWithEmail(email, password)
            if (result.isSuccess) {
                _uiState.value = AuthUiState(user = result.getOrNull())
            } else {
                _uiState.value = AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
