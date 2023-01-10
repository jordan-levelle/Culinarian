package com.cs683.culinarian.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.IngredientEditAdapter
import com.cs683.culinarian.adapters.IngredientItemAdapter
import com.cs683.culinarian.databinding.FragmentRecipeBinding
import com.cs683.culinarian.databinding.FragmentShoppingBinding
import com.cs683.culinarian.viewmodels.RecipeViewModel
import com.cs683.culinarian.viewmodels.ShoppingListViewModel

class ShoppingFragment : Fragment() {
    private var _binding: FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        val shoppingViewModel = ViewModelProvider(this).get(ShoppingListViewModel::class.java)
        binding.viewModel = shoppingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val ingredientAdapter = IngredientItemAdapter(
            shoppingViewModel::shoppingCartClicked
        )
        binding.shoppingList.adapter = ingredientAdapter

        shoppingViewModel.shoppingList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                ingredientAdapter.submitList(list)
            }
        })

        shoppingViewModel.cartClicked.observe(viewLifecycleOwner, Observer { ingredient ->
            ingredient?.let {
                recipeViewModel.removeFromCart(ingredient)
                shoppingViewModel.cartClickHandled()
            }
        })

        shoppingViewModel.cartCleared.observe(viewLifecycleOwner, Observer { cleared ->
            cleared?.let {
                recipeViewModel.clearCart()
                shoppingViewModel.clearHandled()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}