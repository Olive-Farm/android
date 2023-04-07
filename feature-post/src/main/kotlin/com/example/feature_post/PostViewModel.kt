package com.example.feature_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_post.model.UserPostInput
import com.farmer.data.History
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PostViewState())
    val uiState: StateFlow<PostViewState> get() = _uiState.asStateFlow()

    fun setChipState(isSpend: Boolean) {
        _uiState.update {
            it.copy(
                isSpendState = isSpend
            )
        }
    }

    fun postCashData(userPostInput: UserPostInput) {
        _uiState.update {
            it.copy(
                needNameState = userPostInput.name.isEmpty(),
                needAmountState = userPostInput.amount.isEmpty(),
                needDateState = userPostInput.year == 0 || userPostInput.month == 0 || userPostInput.date == 0
            )
        }
        if (userPostInput.name.isEmpty() || userPostInput.amount.isEmpty() || (userPostInput.year == 0 || userPostInput.month == 0 || userPostInput.date == 0)) return
        viewModelScope.launch {
            userPostInput.amount.toIntOrNull()?.let { userInputSpendAmount ->
                val spendTransact = when {
                    _uiState.value.isSpendState.not() -> {
                        History.Transact(
                            earnList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = userPostInput.name
                                )
                            )
                        )
                    }
                    _uiState.value.isSpendState -> {
                        History.Transact(
                            spendList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = userPostInput.name
                                )
                            )
                        )
                    }
                    else -> History.Transact()
                }
                val userInputHistory = History(
                    year = userPostInput.year,
                    month = userPostInput.month,
                    date = userPostInput.date,
                    dayOfWeek = "",
                    tool = "", // todo
                    memo = "", // todo
                    spendList = spendTransact
                )
                _uiState.update {
                    it.copy(dismissDialogState = true)
                }
                repository.insertHistory(userInputHistory)
            }
        }
    }
}
