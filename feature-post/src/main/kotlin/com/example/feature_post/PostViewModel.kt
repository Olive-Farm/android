package com.example.feature_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_post.model.UserPostInput
import com.farmer.data.History
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    fun postCashData(userPostInput: UserPostInput) {
        viewModelScope.launch {
            userPostInput.amount.toIntOrNull()?.let { userInputSpendAmount ->
                val spendTransact = when {
                    userInputSpendAmount > 0 -> {
                        History.Transact(
                            spendList = listOf(
                                History.Transact.TransactData(
                                    price = userInputSpendAmount,
                                    item = userPostInput.name
                                )
                            )
                        )
                    }
                    userInputSpendAmount < 0 -> {
                        History.Transact(
                            earnList = listOf(
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
                repository.insertHistory(userInputHistory)
            }
        }
    }
}
