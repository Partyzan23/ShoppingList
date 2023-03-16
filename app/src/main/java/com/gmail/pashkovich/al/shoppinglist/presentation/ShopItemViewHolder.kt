package com.gmail.pashkovich.al.shoppinglist.presentation

import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pashkovich.al.shoppinglist.R

class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.tvName)
    val tvCount = view.findViewById<TextView>(R.id.tvCount)
}