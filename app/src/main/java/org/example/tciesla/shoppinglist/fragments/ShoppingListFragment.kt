package org.example.tciesla.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.example.tciesla.shoppinglist.R
import org.example.tciesla.shoppinglist.ShoppingListItem
import org.example.tciesla.shoppinglist.ShoppingListItemRecycleAdapter
import org.example.tciesla.shoppinglist.databinding.FragmentShoppingListBinding

var shoppingList = listOf(
    ShoppingListItem("item 1"),
    ShoppingListItem("item 2"),
    ShoppingListItem("item 3"),
)

class ShoppingListFragment : Fragment(), ShoppingListItemCallbacks {

    private lateinit var shoppingListAdapter: ShoppingListItemRecycleAdapter

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        handleShoppingListItemAddedIfOccurred()
        return binding.root
    }

    private fun handleShoppingListItemAddedIfOccurred() {
        val shoppingListItemTitle = arguments?.getString(NEW_SHOPPING_LIST_ITEM_TITLE)
        if (shoppingListItemTitle != null) {
            onShoppingListItemAdded(ShoppingListItem(shoppingListItemTitle))
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

        shoppingListAdapter.submitList(shoppingList)
    }

    override fun onShoppingListItemAdded(shoppingListItem: ShoppingListItem) {
        shoppingList = insertBetweenBoughtAndUnBoughtItems(shoppingListItem)
    }

    override fun onShoppingListItemBought(shoppingListItem: ShoppingListItem) {
        shoppingListItem.bought()

        val listWithoutModifiedItem = shoppingList.filter { it != shoppingListItem }
        shoppingList = if (shoppingListItem.bought) {
            listWithoutModifiedItem.union(listOf(shoppingListItem)).toList()
        } else {
            insertBetweenBoughtAndUnBoughtItems(shoppingListItem)
        }
        shoppingListAdapter.submitList(shoppingList)
    }

    private fun insertBetweenBoughtAndUnBoughtItems(shoppingListItem: ShoppingListItem): List<ShoppingListItem> {
        val unBoughtItems = shoppingList.filter { !it.bought }
        val boughtItems = shoppingList.filter { it.bought }
        return unBoughtItems
            .union(listOf(shoppingListItem)).toList()
            .union(boughtItems).toList()
    }

    override fun onShoppingListItemRemoved(shoppingListItem: ShoppingListItem) {
        shoppingList = shoppingList.filter { it != shoppingListItem }
        shoppingListAdapter.submitList(shoppingList)
    }
}

interface ShoppingListItemCallbacks {
    fun onShoppingListItemAdded(shoppingListItem: ShoppingListItem)
    fun onShoppingListItemBought(shoppingListItem: ShoppingListItem)
    fun onShoppingListItemRemoved(shoppingListItem: ShoppingListItem)
}
