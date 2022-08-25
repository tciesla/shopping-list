package org.example.tciesla.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

data class ShoppingListItem(val title: String)

class ShoppingListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(shoppingListItem: ShoppingListItem) {
        itemView.findViewById<TextView>(R.id.item_title).text = shoppingListItem.title
    }
}

class ShoppingListItemRecycleAdapter :
    ListAdapter<ShoppingListItem, ShoppingListItemViewHolder>(ShoppingListItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.shopping_list_item_row, parent, false)
        return ShoppingListItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        val shoppingListItem = getItem(position)
        holder.bind(shoppingListItem)
    }

    class ShoppingListItemDiffCallback : DiffUtil.ItemCallback<ShoppingListItem>() {
        override fun areItemsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: ShoppingListItem, newItem: ShoppingListItem) =
            oldItem == newItem
    }
}