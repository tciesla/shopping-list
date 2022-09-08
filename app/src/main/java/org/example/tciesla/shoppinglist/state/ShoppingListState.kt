package org.example.tciesla.shoppinglist.state

import android.app.Application
import android.content.Context
import org.example.tciesla.shoppinglist.DEFAULT_SHOPPING_LIST_NAME
import org.example.tciesla.shoppinglist.models.ShoppingListItem
import org.example.tciesla.shoppinglist.repositories.ShoppingListItems

object ShoppingListState : Application() {
    private val listeners: MutableMap<Any, () -> Unit> = hashMapOf()
    private var selectedList: String = DEFAULT_SHOPPING_LIST_NAME

    private lateinit var shoppingListItems: ShoppingListItems
    private lateinit var shoppingList: List<ShoppingListItem>

    fun initialize(context: Context) {
        shoppingListItems = ShoppingListItems(context)
        shoppingList = shoppingListItems.fetch()
    }

    fun register(instance: Any, listener: () -> Unit) {
        listeners[instance] = listener
    }

    fun unregister(instance: Any) {
        listeners.remove(instance)
    }

    fun getSelectedShoppingList(): List<ShoppingListItem> {
        return shoppingList.filter { it.list == selectedList }
    }

    fun getSelectedShoppingListName(): String = selectedList

    fun getShoppingListNames(): List<String> {
        return shoppingList.map { it.list }.toSet().toList()
    }

    fun onShoppingListSelected(list: String) {
        selectedList = list
        notifyListeners()
    }

    fun onShoppingListItemAdded(shoppingListItem: ShoppingListItem) {
        shoppingList = shoppingList.addNewItem(shoppingListItem)
        shoppingListItems.persist(shoppingList)
        notifyListeners()
    }

    fun onShoppingListItemBought(shoppingListItem: ShoppingListItem) {
        shoppingListItem.bought()

        shoppingList = shoppingList.addBoughtItem(shoppingListItem)
        shoppingListItems.persist(shoppingList)
        notifyListeners()
    }

    fun onShoppingListItemRemoved(shoppingListItem: ShoppingListItem) {
        shoppingList = shoppingList.filter { it != shoppingListItem }
        shoppingListItems.persist(shoppingList)
        notifyListeners()
    }

    private fun notifyListeners() {
        listeners.forEach { entry -> entry.value.invoke() }
    }

    private fun List<ShoppingListItem>.addNewItem(item: ShoppingListItem): List<ShoppingListItem> {
        val unBoughtItems = this.filter { !it.bought }
        val boughtItems = this.filter { it.bought }
        return unBoughtItems
            .union(listOf(item)).toList()
            .union(boughtItems).toList()
    }

    private fun List<ShoppingListItem>.addBoughtItem(item: ShoppingListItem): List<ShoppingListItem> {
        val listWithoutModifiedItem = this.filter { it != item }
        return if (item.bought) {
            listWithoutModifiedItem.union(listOf(item)).toList()
        } else {
            this.addNewItem(item)
        }
    }
}
