package org.example.tciesla.shoppinglist.models

data class ShoppingListItem(val title: String, var bought: Boolean = false) {

    fun bought() {
        bought = !bought
    }
}