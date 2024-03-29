package com.farooq.stocks.common.data.domain
sealed class UIComponent(val error: String? = null) {
    data class Dialog(val title: String, val description: String) : UIComponent(description)
    data class None(val message: String) : UIComponent(message)
}