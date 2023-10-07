package com.farmer.home.ui.postdialog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.History
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// todo
@HiltViewModel
class PostDialogViewModel @Inject constructor(
    private val repo: OliveRepository
) : ViewModel() {

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
            }.onFailure {
                Log.e("@@@PostDialogViewModel", "로그가 아닌 진짜 실패 : $it")
            }
        }
    }
}
