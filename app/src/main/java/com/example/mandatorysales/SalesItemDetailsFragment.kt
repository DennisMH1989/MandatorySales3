package com.example.mandatorysales

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mandatorysales.databinding.FragmentSalesItemDetailsBinding
import com.example.mandatorysales.models.SalesItem
import com.example.mandatorysales.models.SalesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.absoluteValue


class SalesItemDetailsFragment : Fragment() {
    private var _binding: FragmentSalesItemDetailsBinding? = null
    private val binding get() = _binding!!
    private val salesViewModel: SalesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalesItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val salesItemDetailsFragmentArgs: SalesItemDetailsFragmentArgs =
            SalesItemDetailsFragmentArgs.fromBundle(bundle)
        val position = salesItemDetailsFragmentArgs.position
        val salesItem = salesViewModel[position]
        if (salesItem == null) {
            binding.textviewMessage.text = "No such sales item!"
            return
        }
        binding.editTextDescription.setText(salesItem.description)
        binding.editTextPrice.setText(salesItem.price.toString())
        binding.editTextSellerEmail.setText(salesItem.sellerEmail)
        binding.editTextSellerPhone.setText(salesItem.sellerPhone)
        // binding.TextViewTime.viewtext(salesItem.time)
        binding.editTextPictureUrl.setText(salesItem.pictureUrl)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        if (Firebase.auth.currentUser?.email == salesItem.sellerEmail)
            binding.buttonDelete.visibility = View.VISIBLE
        else binding.buttonDelete.visibility = View.GONE
        binding.buttonDelete.setOnClickListener {
            salesViewModel.delete(salesItem.id)
            findNavController().popBackStack()
        }

        binding.buttonUpdate.setOnClickListener {
            val description = binding.editTextDescription.text.toString().trim()
            val price = binding.editTextPrice.text.toString().trim().toInt()
            val sellerEmail = binding.editTextSellerEmail.text.toString().trim()
            val sellerPhone = binding.editTextSellerPhone.text.toString().trim()
            val time = binding.TextViewTime.text.toString().trim().toLong()
            val pictureUrl = binding.editTextPictureUrl.text.toString().trim()
            val updatedSalesItem = SalesItem(
                salesItem.id,
                description,
                price,
                sellerEmail,
                sellerPhone,
                time,
                pictureUrl
            )
            Log.d("APPLE", "update $updatedSalesItem")
            salesViewModel.update(updatedSalesItem)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

