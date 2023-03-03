package com.gmail.pashkovich.al.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pashkovich.al.shoppinglist.R
import com.gmail.pashkovich.al.shoppinglist.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    companion object{
        private const val ITEM_TYPE_ENABLED = 100
        private const val ITEM_TYPE_DISABLED = 101
    }

    var shopList = listOf<ShopItem>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutResId = if (viewType == ITEM_TYPE_ENABLED) {
            R.layout.item_shop_enabled
        } else {
            R.layout.item_shop_disabled
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layoutResId,
            parent,
            false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]
        return if (shopItem.enabled) {
            ITEM_TYPE_ENABLED
        } else {
            ITEM_TYPE_DISABLED
        }
    }

    class ShopItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)
    }
}