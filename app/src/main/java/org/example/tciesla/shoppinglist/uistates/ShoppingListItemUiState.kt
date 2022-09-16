package org.example.tciesla.shoppinglist.uistates

data class ShoppingListItemUiState(
    val title: String,
    val list: String,
    val bought: Boolean = false
)