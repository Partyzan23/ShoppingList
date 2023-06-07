package com.gmail.pashkovich.al.shoppinglist.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.gmail.pashkovich.al.shoppinglist.domain.ShopItem
import com.gmail.pashkovich.al.shoppinglist.presentation.ShopListApp
import javax.inject.Inject

class ShopListProvider : ContentProvider() {

    private val component by lazy {
        (context as ShopListApp).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.gmail.pashkovich.al.shoppinglist", "shop_items", GET_SHOP_ITEMS_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when(uriMatcher.match(uri)){
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor()
            }
            else ->{
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when(uriMatcher.match(uri)){
            GET_SHOP_ITEMS_QUERY -> {
                if (values == null) return null
                val id = values.getAsInteger(KEY_ID)
                val name = values.getAsString(KEY_NAME)
                val count = values.getAsInteger(KEY_COUNT)
                val enabled = values.getAsBoolean(KEY_ENABLED)
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                shopListDao.addShopItemSync(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        when(uriMatcher.match(uri)){
            GET_SHOP_ITEMS_QUERY -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                return shopListDao.deleteShopItemSync(id)
            }
        }
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        when(uriMatcher.match(uri)){
            GET_SHOP_ITEMS_QUERY -> {
                if (values == null) return 0
                val id = values.getAsInteger(KEY_ID)
                val name = values.getAsString(KEY_NAME)
                val count = values.getAsInteger(KEY_COUNT)
                val enabled = values.getAsBoolean(KEY_ENABLED)
                val shopItem = ShopItem(
                    id = id,
                    name = name,
                    count = count,
                    enabled = enabled
                )
                return shopListDao.updateShopItem(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return 0
    }

    companion object{
        private const val GET_SHOP_ITEMS_QUERY = 100

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_COUNT = "count"
        const val  KEY_ENABLED = "enabled"
    }
}