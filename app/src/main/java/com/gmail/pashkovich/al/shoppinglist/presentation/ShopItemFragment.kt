package com.gmail.pashkovich.al.shoppinglist.presentation

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gmail.pashkovich.al.shoppinglist.data.ShopListProvider.Companion.KEY_COUNT
import com.gmail.pashkovich.al.shoppinglist.data.ShopListProvider.Companion.KEY_ENABLED
import com.gmail.pashkovich.al.shoppinglist.data.ShopListProvider.Companion.KEY_ID
import com.gmail.pashkovich.al.shoppinglist.data.ShopListProvider.Companion.KEY_NAME
import com.gmail.pashkovich.al.shoppinglist.databinding.FragmentShopItemBinding
import com.gmail.pashkovich.al.shoppinglist.domain.ShopItem
import javax.inject.Inject
import kotlin.concurrent.thread

class ShopItemFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private val component by lazy {
        (requireActivity().application as ShopListApp).component
    }

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private var screenMode = UNDEFINED_SCREEN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners()
        launchRightMode()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

    companion object {
        const val SCREEN_MODE = "mode"
        const val SHOP_ITEM_ID = "shop_item_id"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
        const val UNDEFINED_SCREEN_MODE = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_EDIT -> launchEditModeScreen()
            MODE_ADD -> launchAddModeScreen()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun launchEditModeScreen() {
        viewModel.getShopItem(shopItemId)
        binding.buttonSave.setOnClickListener {
//            viewModel.editShopItem(
//                binding.etName.text?.toString(),
//                binding.etCount.text?.toString()
//            )
            thread {
                context?.contentResolver?.update(
                    Uri.parse("content://com.gmail.pashkovich.al.shoppinglist/shop_items"),
                    ContentValues().apply {
                        put(KEY_ID, shopItemId)
                        put(KEY_NAME, binding.etName.text?.toString())
                        put(KEY_COUNT, binding.etCount.text?.toString())
                        put(KEY_ENABLED, viewModel.shopItem.value?.enabled)
                    },
                    null,
                    null
                )
            }
        }
    }

    private fun launchAddModeScreen() {
        binding.buttonSave.setOnClickListener {
//            viewModel.addShopItem(
//                binding.etName.text?.toString(),
//                binding.etCount.text?.toString()
//            )
            thread {
                context?.contentResolver?.insert(
                    Uri.parse("content://com.gmail.pashkovich.al.shoppinglist/shop_items"),
                    ContentValues().apply {
                        put(KEY_ID, 0)
                        put(KEY_NAME, binding.etName.text?.toString())
                        put(KEY_COUNT, binding.etCount.text?.toString()?.toInt())
                        put(KEY_ENABLED, true)
                    }
                )
            }
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }
}