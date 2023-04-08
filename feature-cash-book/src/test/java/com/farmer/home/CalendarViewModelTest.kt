package com.farmer.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import com.farmer.home.data.CashBookRepository
import com.farmer.home.ui.states.CalendarUiState
import com.farmer.home.ui.states.CalendarViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModelTest {

    private lateinit var testLifecycleOwner: LifecycleOwner
    private val cashBookRepository = mockk<CashBookRepository>()
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        testLifecycleOwner = TestLifecycleOwner()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `값을_입력하면_데이터_추가_후_다이얼로그_끄기`() {
        runTest {
            // given
            val viewModel = CalendarViewModel(cashBookRepository)
            val time = "2022-12-15"
            val name = "버거킹"
            val amount = "-50000"
            coEvery { cashBookRepository.getAllUserData() } returns emptyList()

            // when
            viewModel.sendInputCashDataAndDismiss(time, name, amount)

            // then
            assert(!viewModel.showDialog.value)
        }
    }

    @Test
    fun `화면_초기화시_state는_loading_state`() {
        runTest {
            // given
            coEvery { cashBookRepository.getAllUserData() } returns emptyList()

            // when
            val viewModel = CalendarViewModel(cashBookRepository)

            // then
            assert(viewModel.uiState.value is CalendarUiState.Loading)
        }
    }
}