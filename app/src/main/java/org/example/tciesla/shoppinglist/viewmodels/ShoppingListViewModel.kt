package org.example.tciesla.shoppinglist.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.tciesla.shoppinglist.models.ShoppingListItem
import org.example.tciesla.shoppinglist.repositories.DEFAULT_SHOPPING_LIST_NAME
import org.example.tciesla.shoppinglist.repositories.ShoppingListRepository
import org.example.tciesla.shoppinglist.uistates.ShoppingListItemUiState
import org.example.tciesla.shoppinglist.uistates.ShoppingListUiState

class ShoppingListViewModel : ViewModel() {

    private val _state: MutableLiveData<ShoppingListUiState> = MutableLiveData(ShoppingListUiState())
    val state: LiveData<ShoppingListUiState> = _state

    init {
        viewModelScope.launch {
            fetchShoppingList()
        }
    }

    fun onShoppingListItemAdded(shoppingListItemUiState: ShoppingListItemUiState) {
        ShoppingListRepository.persist(ShoppingListRepository.fetch().addNewItem(ShoppingListItem(
            title = shoppingListItemUiState.title,
            list = shoppingListItemUiState.list,
            bought = shoppingListItemUiState.bought
        )))
        fetchShoppingList()
    }

    fun onShoppingListItemBought(shoppingListItemUiState: ShoppingListItemUiState) {
        ShoppingListRepository.persist(ShoppingListRepository.fetch().addBoughtItem(ShoppingListItem(
            title = shoppingListItemUiState.title,
            list = shoppingListItemUiState.list,
            bought = !shoppingListItemUiState.bought
        )))
        fetchShoppingList()
    }

    fun onShoppingListItemRemoved(shoppingListItemUiState: ShoppingListItemUiState) {
        val removedItem = ShoppingListItem(
            title = shoppingListItemUiState.title,
            list = shoppingListItemUiState.list,
            bought = shoppingListItemUiState.bought
        )
        ShoppingListRepository.persist(ShoppingListRepository.fetch().filter { it != removedItem })
        fetchShoppingList()
    }

    fun onShoppingListSelected(list: String) {
        _state.postValue(
            ShoppingListUiState(
                shoppingList = ShoppingListRepository.fetch().map { item -> ShoppingListItemUiState(item.title, item.list, item.bought) },
                selectedList = list
            )
        )
    }

    private fun fetchShoppingList() {
        _state.postValue(
            ShoppingListUiState(
                shoppingList = ShoppingListRepository.fetch().map { item -> ShoppingListItemUiState(item.title, item.list, item.bought) },
                selectedList = _state.value?.selectedList ?: DEFAULT_SHOPPING_LIST_NAME
            )
        )
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