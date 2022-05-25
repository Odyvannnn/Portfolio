package com.length.icthack3.presentation.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.length.icthack3.R
import com.length.icthack3.presentation.adapters.RateAdapter
import com.length.icthack3.presentation.viewModels.RateViewModel

class RateFragment: Fragment() {
    companion object {
        fun newInstance() = RateFragment()
    }

    private val viewModel: RateViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    private val adapter = RateAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.rate_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    val navController = Navigation.findNavController(requireActivity(), R.id.container)
                    navController.navigate(R.id.action_rateFragment_to_gameFragment2)
                }
            }
        this.recyclerView = view.findViewById(R.id.rateRecycler)
        requireActivity().onBackPressedDispatcher.addCallback(this@RateFragment, callback)
        initRecyclerView()
        viewModel.usersData.observe(this) {
            Log.d("users", viewModel.usersData.toString())
            adapter.users = it
        }
        viewModel.getUserList()
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }
}