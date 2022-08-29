package org.example.tciesla.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.example.tciesla.shoppinglist.R
import org.example.tciesla.shoppinglist.ShoppingListItemRecycleAdapter
import org.example.tciesla.shoppinglist.databinding.FragmentShoppingListBinding
import org.example.tciesla.shoppinglist.models.ShoppingListItem
import org.example.tciesla.shoppinglist.repositories.ShoppingListItems

class ShoppingListFragment : Fragment(), ShoppingListItemCallbacks {

    private lateinit var shoppingListItems: ShoppingListItems
    private lateinit var shoppingListAdapter: ShoppingListItemRecycleAdapter

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoppingListItems = ShoppingListItems(requireContext())
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        handleShoppingListItemAddedIfOccurred()
        return binding.root
    }

    private fun handleShoppingListItemAddedIfOccurred() {
        val shoppingListItemTitle = arguments?.getString(NEW_SHOPPING_LIST_ITEM_TITLE)
        if (shoppingListItemTitle != null) {
            onShoppingListItemAdded(ShoppingListItem(shoppingListItemTitle, bought = false))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpShoppingListItemRecycleAdapter()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpShoppingListItemRecycleAdapter() {
        shoppingListAdapter = ShoppingListItemRecycleAdapter(this)

        binding.shoppingList.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = shoppingListAdapter
        }

        shoppingListAdapter.submitList(shoppingListItems.fetch())
    }

    override fun onShoppingListItemAdded(shoppingListItem: ShoppingListItem) {
        val shoppingList = shoppingListItems.fetch()
        val modifiedShoppingList = shoppingList.addNewItem(shoppingListItem)
        shoppingListItems.persist(modifiedShoppingList)
    }

    override fun onShoppingListItemBought(shoppingListItem: ShoppingListItem) {
        shoppingListItem.bought()

        val shoppingList = shoppingListItems.fetch()
        val modifiedShoppingList = shoppingList.addBoughtItem(shoppingListItem)
        shoppingListAdapter.submitList(modifiedShoppingList)
        shoppingListItems.persist(modifiedShoppingList)
    }

    override fun onShoppingListItemRemoved(shoppingListItem: ShoppingListItem) {
        val shoppingList = shoppingListItems.fetch()
        val modifiedShoppingList = shoppingList.filter { it != shoppingListItem }
        shoppingListAdapter.submitList(modifiedShoppingList)
        shoppingListItems.persist(modifiedShoppingList)
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

interface ShoppingListItemCallbacks {
    fun onShoppingListItemAdded(shoppingListItem: ShoppingListItem)
    fun onShoppingListItemBought(shoppingListItem: ShoppingListItem)
    fun onShoppingListItemRemoved(shoppingListItem: ShoppingListItem)
}
