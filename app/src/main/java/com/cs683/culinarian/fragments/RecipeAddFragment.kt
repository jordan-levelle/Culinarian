package com.cs683.culinarian.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.IngredientEditAdapter
import com.cs683.culinarian.adapters.InstructionEditAdapter
import com.cs683.culinarian.databinding.FragmentRecipeEditBinding
import com.cs683.culinarian.utilities.IngredientDragCallback
import com.cs683.culinarian.utilities.InstructionDragCallback
import com.cs683.culinarian.viewmodels.RecipeViewModel

class RecipeAddFragment : Fragment() {
    private var _binding: FragmentRecipeEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeEditBinding.inflate(inflater, container, false)
        val view = binding.root

        //build view model and bind to layout
        val viewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        viewModel.setRecipe(0L)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //create adapters with listener functions, bind to recycler views
        val ingredientAdapter = IngredientEditAdapter (
            viewModel::updateIngredient,
            viewModel::moveIngredient,
            viewModel::deleteIngredient
        )
        val instructionAdapter = InstructionEditAdapter (
            viewModel::updateInstruction,
            viewModel::moveInstruction,
            viewModel::deleteInstruction
        )
        binding.ingredientList.adapter = ingredientAdapter
        binding.instructionList.adapter = instructionAdapter

        //attach drag and drop callback to recycler views
        ItemTouchHelper(IngredientDragCallback()).attachToRecyclerView(binding.ingredientList)
        ItemTouchHelper(InstructionDragCallback()).attachToRecyclerView(binding.instructionList)

        //observe view model data, pass to adapter
        viewModel.recipe.observe(viewLifecycleOwner, Observer {
            it?.let {
                ingredientAdapter.submitList(it.ingredients)
                instructionAdapter.submitList(it.instructions)
            }
        })

        viewModel.cancelEdit.observe(viewLifecycleOwner, Observer { cancel ->
            if(cancel) {
                view.findNavController().navigate(R.id.action_recipeAddFragment_pop)
                viewModel.updateCanceled()
            }
        })

        viewModel.submitRecipe.observe(viewLifecycleOwner, Observer { submit ->
            if(submit) {
                viewModel.addRecipe()
                view.findNavController().navigate(R.id.action_recipeAddFragment_to_recipeFragment)
            }
        })

        viewModel.submitToFolder.observe(viewLifecycleOwner, Observer { submit ->
            if(submit) {
                viewModel.addRecipe()
                view.findNavController().navigate(R.id.action_recipeAddFragment_pop)
            }
        })

        //observe when ingredients and instructions changed, notify adapter
        viewModel.addedIngredient.observe(viewLifecycleOwner, Observer { added ->
            added?.let {
                ingredientAdapter.onItemAdded()
                viewModel.addedIngredient()
            }
        })

        viewModel.deletedIngredient.observe(viewLifecycleOwner, Observer { deleted ->
            deleted?.let {
                ingredientAdapter.onItemDeleted(deleted)
                viewModel.deletedIngredient()
            }
        })

        viewModel.addedInstruction.observe(viewLifecycleOwner, Observer { added ->
            added?.let {
                instructionAdapter.onItemAdded()
                viewModel.addedInstruction()
            }
        })

        viewModel.deletedInstruction.observe(viewLifecycleOwner, Observer {deleted ->
            deleted?.let {
                instructionAdapter.onItemDeleted(deleted)
                viewModel.deletedInstruction
            }
        })

        return view
    }
}