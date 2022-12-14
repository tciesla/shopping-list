package org.example.tciesla.shoppinglist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.example.tciesla.shoppinglist.uistates.ShoppingListItemUiState
import org.example.tciesla.shoppinglist.viewmodels.ShoppingListViewModel

class ShoppingListItemViewHolder(private val shoppingListViewModel: ShoppingListViewModel, itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(shoppingListItem: ShoppingListItemUiState) {
        val itemTitleView = itemView.findViewById<TextView>(R.id.item_title)

        itemTitleView.text = shoppingListItem.title
        setPaintFlags(itemTitleView, shoppingListItem.bought)

        itemView.setOnClickListener {
            shoppingListViewModel.onShoppingListItemBought(shoppingListItem)
            setPaintFlags(itemTitleView, shoppingListItem.bought)
        }
        itemView.setOnLongClickListener {
            shoppingListViewModel.onShoppingListItemRemoved(shoppingListItem)
            return@setOnLongClickListener true
        }
    }

    private fun setPaintFlags(itemTitleView: TextView, bought: Boolean) {
        if (bought) {
            itemTitleView.paintFlags = itemTitleView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            itemTitleView.paintFlags = itemTitleView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

class ShoppingListItemRecycleAdapter(private val shoppingListViewModel: ShoppingListViewModel) : ListAdapter<ShoppingListItemUiState, ShoppingListItemViewHolder>(ShoppingListItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.shopping_list_item_row, parent, false)
        return ShoppingListItemViewHolder(shoppingListViewModel, itemView)
    }

    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        val shoppingListItem = getItem(position)
        holder.bind(shoppingListItem)
    }

    class ShoppingListItemDiffCallback : DiffUtil.ItemCallback<ShoppingListItemUiState>() {
        override fun areItemsTheSame(oldItem: ShoppingListItemUiState, newItem: ShoppingListItemUiState) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ShoppingListItemUiState, newItem: ShoppingListItemUiState) =
            oldItem == newItem
    }
}