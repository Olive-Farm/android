package com.farmer.feature_settings

import androidx.lifecycle.ViewModel
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditCategoryViewState())

    val uiState: StateFlow<EditCategoryViewState> get() = _uiState.asStateFlow()

    fun refreshState() {
        _uiState.update { EditCategoryViewState() }
    }

}