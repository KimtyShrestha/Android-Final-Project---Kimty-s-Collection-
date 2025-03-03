package com.example.a35b_crud.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a35b_crud.databinding.FragmentHomeBinding
import com.example.a35b_crud.model.Product
import com.example.a35b_crud.ui.adapter.ProductAdapter
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // Non-null assertion operator

    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchProductsFromFirestore()
    }

    private fun setupRecyclerView() {
        // Pass a lambda function to handle delete clicks
        productAdapter = ProductAdapter(productList) { product ->
            deleteProductFromFirestore(product)
        }
        binding.recyclerViewProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
        }
    }

    private fun fetchProductsFromFirestore() {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                productList.clear()
                for (document in result) {
                    val product = document.toObject(Product::class.java)
                    product.id = document.id // Set the Firestore document ID to the product
                    productList.add(product)
                }
                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    private fun deleteProductFromFirestore(product: Product) {
        db.collection("products")
            .document(product.id) // Use the Firestore document ID to delete the product
            .delete()
            .addOnSuccessListener {
                // Remove the product from the list and notify the adapter
                productList.remove(product)
                productAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}