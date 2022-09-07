package org.example.tciesla.shoppinglist.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.example.tciesla.shoppinglist.DEFAULT_SHOPPING_LIST_NAME
import org.example.tciesla.shoppinglist.R
import org.example.tciesla.shoppinglist.databinding.FragmentShoppingListItemAddBinding

const val NEW_SHOPPING_LIST_ITEM_TITLE = "newShoppingListItemTitle"
const val NEW_SHOPPING_LIST_ITEM_LIST = "newShoppingListItemList"

class ShoppingListItemAddFragment : Fragment() {

    private var _binding: FragmentShoppingListItemAddBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListItemAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAdd.setOnClickListener {
            val bundle = bundleOf(
                NEW_SHOPPING_LIST_ITEM_TITLE to view.findViewById<EditText>(R.id.item_title).text.toString(),
                NEW_SHOPPING_LIST_ITEM_LIST to view.findViewById<EditText>(R.id.item_list).text.toStringOrDefault()
            )
            findNavController().navigate(R.id.action_ShoppingListItemAddFragment_to_ShoppingListFragment, bundle)
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigate(R.id.action_ShoppingListItemAddFragment_to_ShoppingListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Editable.toStringOrDefault(): String {
    return this.toString().ifBlank {
        DEFAULT_SHOPPING_LIST_NAME
    }
}
