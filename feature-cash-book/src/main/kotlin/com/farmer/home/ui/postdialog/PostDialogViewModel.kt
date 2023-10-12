package com.farmer.home.ui.postdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.History
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// todo
@HiltViewModel
class PostDialogViewModel @Inject constructor(
    private val repo: OliveRepository
) : ViewModel() {

    private val _deletedId: MutableStateFlow<List<Long>> = MutableStateFlow(emptyList())
    val deletedId = _deletedId.asStateFlow()

    // id로 내역 삭제
    fun deleteHistory(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            val historyId = history.id
            if (historyId != null) {
                repo.deleteHistory(history)
            }
        }
    }


    fun deleteTransactionData(
        historyId: Long?,
        transactionId: Long
    ) {
        if (historyId == null) return
        viewModelScope.launch {
            kotlin.runCatching {
                repo.deleteTransactionData(historyId, transactionId)
            }.onSuccess {
                _deletedId.update {
                    it + transactionId
                }
            }.onFailure {
                Log.e("@@@PostDialogViewModel", "로그가 아닌 진짜 실패 : $it")
            }
        }
    }
}
