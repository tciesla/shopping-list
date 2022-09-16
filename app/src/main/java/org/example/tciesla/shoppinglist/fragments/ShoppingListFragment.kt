package org.example.tciesla.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.example.tciesla.shoppinglist.R
import org.example.tciesla.shoppinglist.ShoppingListItemRecycleAdapter
import org.example.tciesla.shoppinglist.databinding.FragmentShoppingListBinding
import org.example.tciesla.shoppinglist.viewmodels.ShoppingListViewModel

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    private val shoppingListViewModel: ShoppingListViewModel by activityViewModels()
    private lateinit var shoppingListAdapter: ShoppingListItemRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shoppingListAdapter = ShoppingListItemRecycleAdapter(shoppingListViewModel)

        shoppingListViewModel.state.observe(this) { state ->
            shoppingListAdapter.submitList(state.shoppingList.filter { it.list == state.selectedList })
        }
    }

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

        binding.shoppingList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shoppingListAdapter
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_ShoppingListFragment_to_ShoppingListItemAddFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
