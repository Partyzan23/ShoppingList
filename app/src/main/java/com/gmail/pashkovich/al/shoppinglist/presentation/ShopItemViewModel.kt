package com.gmail.pashkovich.al.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.gmail.pashkovich.al.shoppinglist.data.ShopListRepositoryImpl
import com.gmail.pashkovich.al.shoppinglist.domain.AddShopItemUseCase
import com.gmail.pashkovich.al.shoppinglist.domain.EditShopItemUseCase
import com.gmail.pashkovich.al.shoppinglist.domain.GetShopItemUseCase
import com.gmail.pashkovich.al.shoppinglist.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count,true)
            addShopItemUseCase.addShopItem(shopItem)
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count,true)
            editShopItemUseCase.editShopItem(shopItem)
        }
    }

    fun getShopItem(shopItemID: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemID)
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int{
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            //TODO: show error inputName
            result = false
        }
        if (count <= 0) {
            //TODO: show error inputCount
            result = false
        }
        return result
    }
}