package com.vegimhasani.bux.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vegimhasani.bux.R
import com.vegimhasani.bux.databinding.MainFragmentBinding
import com.vegimhasani.bux.detail.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    private val clickListener: ((String) -> Unit) = {
        navigateToDetails(it)
    }
    private val adapter = ProductsAdapter(clickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is MainState.DisplayData -> {
                    displayData()
                }
            }
        }
    }

    private fun navigateToDetails(productId: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailFragment.newInstance(productId))
            .addToBackStack(null)
            .commit()
    }

    private fun displayData() {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
    }
}
