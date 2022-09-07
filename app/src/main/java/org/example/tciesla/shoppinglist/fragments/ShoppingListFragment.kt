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
import org.example.tciesla.shoppinglist.state.ShoppingListState

class ShoppingListFragment : Fragment() {

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
        val shoppingListItemList = arguments?.getString(NEW_SHOPPING_LIST_ITEM_LIST)
        if (shoppingListItemTitle != null && shoppingListItemList != null) {
            ShoppingListState.onShoppingListItemAdded(
                ShoppingListItem(shoppingListItemTitle, shoppingListItemList, bought = false)
            )
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
        ShoppingListState.unregister(this)
    }

    private fun setUpShoppingListItemRecycleAdapter() {
        shoppingListAdapter = ShoppingListItemRecycleAdapter()

        binding.shoppingList.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = shoppingListAdapter
        }

        shoppingListAdapter.submitList(ShoppingListState.getSelectedShoppingList())

        ShoppingListState.register(this) {
            shoppingListAdapter.submitList(ShoppingListState.getSelectedShoppingList())
        }
    }

}
