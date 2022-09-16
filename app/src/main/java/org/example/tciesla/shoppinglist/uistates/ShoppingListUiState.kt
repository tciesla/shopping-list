package org.example.tciesla.shoppinglist.uistates

import org.example.tciesla.shoppinglist.repositories.DEFAULT_SHOPPING_LIST_NAME

data class ShoppingListUiState(
    val shoppingList: List<ShoppingListItemUiState> = listOf(),
    val selectedList: String = DEFAULT_SHOPPING_LIST_NAME
)