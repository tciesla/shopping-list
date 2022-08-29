package org.example.tciesla.shoppinglist.models

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingListItem(val title: String, var bought: Boolean) {

    fun bought() {
        bought = !bought
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ShoppingListItem
        if (title != other.title) return false
        return true
    }

    override fun hashCode(): Int {
        return title.hashCode()
    }

}