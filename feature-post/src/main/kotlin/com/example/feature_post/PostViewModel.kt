package com.example.feature_post

import androidx.lifecycle.ViewModel
import com.example.feature_post.model.UserPostInput
import com.farmer.data.History
import com.farmer.data.repository.OliveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: OliveRepository
) : ViewModel() {
    fun postCashData(userPostInput: UserPostInput) {
        val userInputHistory = History(
            year = userPostInput.year,
            month = userPostInput.month,
            date = userPostInput.month,
            dayOfWeek = "MON",
            tool = "card",
            memo = "memo"
        )

//        repository.insertHistory(
//
//        )
    }
}
