package com.length.icthack3.presentation.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.length.icthack3.R
import com.length.icthack3.domain.model.Animal
import com.length.icthack3.domain.model.User
import com.length.icthack3.presentation.viewModels.ShopViewModel

class ShopFragment : Fragment() {

    private val viewModel: ShopViewModel by viewModels()

    private lateinit var balanceTextView: TextView
    private lateinit var pigImageView: ImageView
    private lateinit var gooseImageView: ImageView
    private lateinit var goatImageView: ImageView
    private lateinit var buyPigButton: Button
    private lateinit var buyGooseButton: Button
    private lateinit var buyGoatButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.shop_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateUser()
        initViews(view)
        initBackPress()
        buyPigButton.setOnClickListener{
            viewModel.buyAnimal(Animal.TYPE_PIG)
        }
        buyGoatButton.setOnClickListener{
            viewModel.buyAnimal(Animal.TYPE_GOAT)
        }
        buyGooseButton.setOnClickListener{
            viewModel.buyAnimal(Animal.TYPE_GOOSE)
        }

        viewModel.user.observe(this){
            changeUIBalance()
        }
    }

    private fun initViews(view: View) {
        balanceTextView = view.findViewById(R.id.balance_text_view)
        pigImageView = view.findViewById(R.id.pig_image_view)
        gooseImageView = view.findViewById(R.id.goose_image_view)
        goatImageView = view.findViewById(R.id.goat_image_view)
        buyPigButton = view.findViewById(R.id.buy_pig_button)
        buyGooseButton = view.findViewById(R.id.buy_goose_button)
        buyGoatButton = view.findViewById(R.id.buy_goat_button)
    }

    private fun initBackPress() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val navController =
                        Navigation.findNavController(requireActivity(), R.id.container)
                    navController.navigate(R.id.action_shopFragment_to_gameFragment2)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this@ShopFragment, callback)
    }

    private fun changeUIBalance(){
        balanceTextView.text = viewModel.user.value!!.balance.toString()
    }
}