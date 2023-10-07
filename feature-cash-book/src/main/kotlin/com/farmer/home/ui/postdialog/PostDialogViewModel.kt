package com.farmer.home.ui.postdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmer.data.History
import com.farmer.data.OliveDao
import com.farmer.data.repository.OliveRepository
import com.farmer.home.data.CashBookRepository
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

    fun deleteHistory(
        history: History,
        index: Int
    ) {

    }
}
