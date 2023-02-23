package com.gmail.pashkovich.al.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gmail.pashkovich.al.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private var count = 0

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            if (count == 0) {
                count++
                val item = it[0]
                viewModel.deleteShopItem(item)
            }
            Log.d("MainActivityTest", it.toString())
        }
    }
}