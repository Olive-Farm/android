package com.farmer.feature_settings

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.Category
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditCategoryViewState())

    val newCategoryName = mutableStateOf(TextFieldValue(""))

    val uiState: StateFlow<EditCategoryViewState> get() = _uiState.asStateFlow()

    fun refreshState() {
        _uiState.update { EditCategoryViewState() }
    }

    fun setAddState() {
        _uiState.update {
            it.copy(
                addState = !uiState.value.addState
            )
        }
    }

    fun selectCategoryList(): List<Category>? {
        return repository.getAllCategory()
    }

    fun addCategory() {
        val currentName = newCategoryName.value.text
        _uiState.update {
            it.copy(
                needNameState = currentName.isEmpty()
            )
        }

        if (currentName.isEmpty()) return
        viewModelScope.launch{

            val newCategory = Category(categoryname = currentName)

            _uiState.update { it.copy(dismissDialogState = true) }
            repository.insertCategory(newCategory)
            newCategoryName.value = TextFieldValue("")
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch{
            repository.deleteCategory(categoryId)
        }
    }


}