package com.example.mandatorysales

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mandatorysales.databinding.FragmentAllSalesItemsBinding
import com.example.mandatorysales.models.MyAdapter
import com.example.mandatorysales.models.SalesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AllSalesItemsFragment : Fragment() {
    private var _binding: FragmentAllSalesItemsBinding? = null
    private val binding get() = _binding!!

    private val salesViewModel: SalesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllSalesItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        salesViewModel.salesItemsLiveData.observe(viewLifecycleOwner) { salesItems ->
            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (salesItems == null) View.GONE else View.VISIBLE
            if (salesItems != null) {
                val adapter = MyAdapter(salesItems) { position ->
                    val action =
                        AllSalesItemsFragmentDirections.actionAllSalesItemsFragmentToSalesItemDetailsFragment(
                            position
                        )
                    findNavController().navigate(action/*R.id.action_AllSalesItemsFragment_to_SalesItemDetailsFragment*/)
                }
                var columns = 4
                val currentOrientation = this.resources.configuration.orientation
                if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    columns = 2
                } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    columns = 4
                }
                binding.recyclerView.layoutManager = GridLayoutManager(this.context, columns)

                binding.recyclerView.adapter = adapter
            }
        }

        binding.textviewHallo.text = "Welcome " + Firebase.auth.currentUser?.email


        salesViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textviewMessage.text = errorMessage
        }

        salesViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            salesViewModel.reload()
            binding.swiperefresh.isRefreshing = false
        }

        salesViewModel.salesItemsLiveData.observe(viewLifecycleOwner) { salesItems ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, salesItems)
            binding.spinnerSalesitems.adapter = adapter
        }

        binding.buttonShowDetails.setOnClickListener {
            val position = binding.spinnerSalesitems.selectedItemPosition
            val action =
                AllSalesItemsFragmentDirections.actionAllSalesItemsFragmentToSalesItemDetailsFragment(
                    position
                )
            findNavController().navigate(action/*R.id.action_AllSalesItemsFragment_to_SalesItemDetailsFragment*/)
        }

        binding.buttonSort.setOnClickListener {
            when (binding.spinnerSorting.selectedItemPosition) {
                0 -> salesViewModel.sortByDescription()
                1 -> salesViewModel.sortByDescriptionDescending()
                2 -> salesViewModel.sortByPrice()
                3 -> salesViewModel.sortByPriceDescending()
            }
        }
        binding.buttonFilter.setOnClickListener {
            val description = binding.edittextFilterDescription.text.toString().trim()
            salesViewModel.filterByDescription(description)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}