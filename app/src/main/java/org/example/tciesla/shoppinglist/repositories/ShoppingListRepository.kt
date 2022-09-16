package org.example.tciesla.shoppinglist.repositories

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.tciesla.shoppinglist.models.ShoppingListItem

const val DEFAULT_SHOPPING_LIST_NAME = "default"
const val SHARED_PREFERENCES_KEY = "list"
const val EMPTY_LIST = "[]"

object ShoppingListRepository {

    private lateinit var preferences: SharedPreferences

    fun initialize(context: Context) {
        preferences = context.getSharedPreferences("${this::class.qualifiedName}", Context.MODE_PRIVATE)
    }

    fun fetch(): List<ShoppingListItem> {
        return Json.decodeFromString(
            preferences.getString(SHARED_PREFERENCES_KEY, EMPTY_LIST) ?: EMPTY_LIST
        )
    }

    fun persist(shoppingList: List<ShoppingListItem>) {
        val shoppingListAsJson = Json.encodeToString(shoppingList)
        with (preferences.edit()) {
            putString(SHARED_PREFERENCES_KEY, shoppingListAsJson)
            apply()
        }
    }
}