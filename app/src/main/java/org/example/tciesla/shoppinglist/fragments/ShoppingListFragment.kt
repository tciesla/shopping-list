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
import org.example.tciesla.shoppinglist.state.ShoppingListState

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpShoppingListItemRecycleAdapter()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_ShoppingListFragment_to_ShoppingListItemAddFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        ShoppingListState.unregister(this)
    }

    private fun setUpShoppingListItemRecycleAdapter() {
        val shoppingListAdapter = ShoppingListItemRecycleAdapter()

        binding.shoppingList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shoppingListAdapter
        }

        shoppingListAdapter.submitList(ShoppingListState.getSelectedShoppingList())

        ShoppingListState.register(this) {
            shoppingListAdapter.submitList(ShoppingListState.getSelectedShoppingList())
        }
    }

}
