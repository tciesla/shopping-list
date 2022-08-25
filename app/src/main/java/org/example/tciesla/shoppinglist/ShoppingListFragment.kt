package org.example.tciesla.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.example.tciesla.shoppinglist.databinding.FragmentShoppingListBinding

var shoppingList = listOf(
    ShoppingListItem("item 1"),
    ShoppingListItem("item 2"),
    ShoppingListItem("item 3"),
)

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        handleNewShoppingListItemIfExists()
        return binding.root
    }

    private fun handleNewShoppingListItemIfExists() {
        val newShoppingListItemTitle = arguments?.getString(NEW_SHOPPING_LIST_ITEM_TITLE)
        if (newShoppingListItemTitle != null) {
            val newShoppingListItem = ShoppingListItem(newShoppingListItemTitle)
            shoppingList = shoppingList.union(listOf(newShoppingListItem)).toList()
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
        val shoppingListItemRecycleAdapter = ShoppingListItemRecycleAdapter()

        binding.shoppingList.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = shoppingListItemRecycleAdapter
        }

        shoppingListItemRecycleAdapter.submitList(shoppingList)
    }
}