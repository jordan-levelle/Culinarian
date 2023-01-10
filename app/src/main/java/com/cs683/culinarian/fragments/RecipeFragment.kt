package com.cs683.culinarian.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.IngredientItemAdapter
import com.cs683.culinarian.adapters.InstructionItemAdapter
import com.cs683.culinarian.databinding.FragmentRecipeBinding
import com.cs683.culinarian.model.Recipe
import com.cs683.culinarian.viewmodels.RecipeViewModel

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get binding object, inflate view
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        val view = binding.root

        //build view model and bind to layout
        viewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //bind recycler views to adapters
        val ingredientAdapter = IngredientItemAdapter(
            viewModel::shoppingCartClicked
        )
        val instructionAdapter = InstructionItemAdapter()
        binding.ingredientList.adapter = ingredientAdapter
        binding.instructionList.adapter = instructionAdapter
        ingredientAdapter.refreshList()

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser) {
                viewModel.ratingChanged(rating.toInt())
            }
        }

        //pass data from view model to adapter
        viewModel.recipe.observe(viewLifecycleOwner, Observer {
            it?.let {
                ingredientAdapter.submitList(it.ingredients)
                instructionAdapter.submitList(it.instructions)
            }
        })

        //observer when to navigate to recipe edit fragment
        viewModel.navigateToRecipe.observe(viewLifecycleOwner, Observer {navigate ->
            if(navigate) {
                view.findNavController().navigate(R.id.action_recipeFragment_to_recipeEditFragment)
                viewModel.onRecipeNavigated()
            }
        })

        viewModel.addAllToCart.observe(viewLifecycleOwner, Observer { addToCart ->
            if(addToCart) {
                ingredientAdapter.refreshList()
                viewModel.addedToCart()
            }
        })

        viewModel.navigateToRelations.observe(viewLifecycleOwner, Observer {navigate ->
            if(navigate) {
                view.findNavController().navigate(R.id.action_recipeFragment_to_recipeFolderRelationFragment)
                viewModel.onRelationsNavigated()
            }
        })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}