package com.example.a35b_crud.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a35b_crud.databinding.FragmentAddProductBinding
import com.example.a35b_crud.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!! // Non-null assertion operator

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle "Add Product" button click
        binding.btnAddProduct.setOnClickListener {
            addProductToFirestore()
        }
    }

    private fun addProductToFirestore() {
        // Get input values using binding
        val name = binding.etProductName.text.toString().trim()
        val description = binding.etProductDescription.text.toString().trim()
        val price = binding.etProductPrice.text.toString().trim().toDoubleOrNull() ?: 0.0
        val imageUrl = binding.etProductImageUrl.text.toString().trim()

        // Validate inputs
        if (name.isEmpty() || description.isEmpty() || price <= 0 || imageUrl.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new product
        val product = Product(
            id = System.currentTimeMillis().toString(), // Generate a unique ID
            name = name,
            description = description,
            price = price,
            imageUrl = imageUrl
        )

        // Add the product to Firestore
        db.collection("products")
            .document(product.id)
            .set(product)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show()
                clearForm()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to add product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        binding.etProductName.text.clear()
        binding.etProductDescription.text.clear()
        binding.etProductPrice.text.clear()
        binding.etProductImageUrl.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up the binding to avoid memory leaks
    }
}