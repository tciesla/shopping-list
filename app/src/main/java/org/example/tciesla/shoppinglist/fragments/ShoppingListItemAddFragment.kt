package org.example.tciesla.shoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.example.tciesla.shoppinglist.R
import org.example.tciesla.shoppinglist.databinding.FragmentShoppingListItemAddBinding
import org.example.tciesla.shoppinglist.models.ShoppingListItem
import org.example.tciesla.shoppinglist.state.DEFAULT_SHOPPING_LIST_NAME
import org.example.tciesla.shoppinglist.state.ShoppingListState

class ShoppingListItemAddFragment : Fragment() {

    private var _binding: FragmentShoppingListItemAddBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListItemAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener {
            ShoppingListState.onShoppingListItemAdded(newShoppingListItem(view))
            navigateToShoppingListFragment()
        }

        binding.buttonCancel.setOnClickListener {
            navigateToShoppingListFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun navigateToShoppingListFragment() {
        findNavController().navigate(R.id.action_ShoppingListItemAddFragment_to_ShoppingListFragment)
    }

    private fun newShoppingListItem(view: View): ShoppingListItem {
        return ShoppingListItem(
            title = view.findEditTextString(R.id.item_title, "item"),
            list = view.findEditTextString(R.id.item_list, DEFAULT_SHOPPING_LIST_NAME),
            bought = false
        )
    }

    private fun View.findEditTextString(viewId: Int, default: String): String {
        return this.findViewById<EditText>(viewId).text.toString().trim().ifBlank { default }
    }
}
