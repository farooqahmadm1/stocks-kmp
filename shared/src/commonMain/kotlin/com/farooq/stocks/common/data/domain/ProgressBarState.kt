package com.farooq.stocks.common.data.domain
sealed class ProgressBarState {
    object Loading : ProgressBarState()
    object Idle : ProgressBarState()
}