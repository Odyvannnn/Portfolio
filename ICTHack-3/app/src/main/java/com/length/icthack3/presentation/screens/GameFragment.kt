package com.length.icthack3.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.length.icthack3.R
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.length.icthack3.presentation.viewModels.GameViewModel


class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profile = view.findViewById<FloatingActionButton>(R.id.buttonProfile)
        val rate = view.findViewById<FloatingActionButton>(R.id.buttonRate)
        val shop = view.findViewById<FloatingActionButton>(R.id.buttonShop)
        val profileInfo = view.findViewById<CardView>(R.id.profileUserCard)
        val profileUsername = view.findViewById<TextView>(R.id.profileUsername)
        val profileUserBalance = view.findViewById<TextView>(R.id.profileBalance)
        val cardView = view.findViewById<CardView>(R.id.cardVIew)
        val counter = view.findViewById<TextView>(R.id.counter)
        var ct = 0
        counter.text = ct.toString()
        cardView.setOnClickListener {
            ct += 1
            counter.text = ct.toString()
        }

        viewModel.user.observe(this){
            profileUsername.text = "Логин: ${viewModel.user.value?.token}"
            profileUserBalance.text = "Баланс: ${viewModel.user.value?.balance}"
        }



        viewModel.getAnimals()

        profile.setOnClickListener{
            navigateToProfileFragment(profileInfo)
        }

        rate.setOnClickListener{
            navigateToRateFragment()
        }

        shop.setOnClickListener {
            navigateToShopFragment()
        }
    }

//    Replace with navigation by bottomNavBar
    private fun navigateToProfileFragment(profileInfo: CardView){
    val mFadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.nav_default_enter_anim)
    val mFadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.nav_default_exit_anim)

    if (profileInfo.visibility == View.INVISIBLE) {
        profileInfo.startAnimation(mFadeInAnimation)
        profileInfo.visibility = View.VISIBLE
    }else{
        profileInfo.startAnimation(mFadeOutAnimation)
        profileInfo.visibility = View.INVISIBLE
    }

    }

    private fun navigateToRateFragment(){
        val navController = Navigation.findNavController(requireActivity(), R.id.container)
        navController.navigate(R.id.action_gameFragment_to_rateFragment)
    }

    private fun navigateToShopFragment(){
        val navController = Navigation.findNavController(requireActivity(), R.id.container)
        navController.navigate(R.id.action_gameFragment_to_shopFragment)
    }

}