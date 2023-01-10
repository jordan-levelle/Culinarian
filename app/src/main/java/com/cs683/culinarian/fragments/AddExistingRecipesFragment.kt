package com.cs683.culinarian.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cs683.culinarian.adapters.AddExistingRecipeAdapter
import com.cs683.culinarian.databinding.AddExistingRecipeBinding
import com.cs683.culinarian.viewmodels.RecipesListViewModel


class AddExistingRecipesFragment: Fragment() {
    private var _binding: AddExistingRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var addViewModel: RecipesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddExistingRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addViewModel = ViewModelProvider(this).get(RecipesListViewModel::class.java)

        val folderId = AddExistingRecipesFragmentArgs.fromBundle(requireArguments()).folderId

        addViewModel.setAddFolderId(folderId.toInt())

        val addAdapter = AddExistingRecipeAdapter(addViewModel::addCheckedRecipes)

        binding.apply {
            addExistingRecipeList.apply {
                adapter = addAdapter
            }

            addViewModel._recipes.observe(viewLifecycleOwner, Observer {
                addAdapter.replaceItems(it)
            })

            addViewModel.navigateToFolder.observe(viewLifecycleOwner, Observer { id ->
                id?.let {
                    okClick(folderId)
                }
            })

            binding.okRecipeList.setOnClickListener { addViewModel.submitRecipes() }
            binding.cancelRecipeList.setOnClickListener { cancelClick(folderId) }
        }
    }

    private fun okClick(folderId: Long) {
        val action =
            AddExistingRecipesFragmentDirections.actionAddExistingRecipesFragmentToRecipesFragment(
                folderId
            )
        this.findNavController().navigate(action)
    }

    private fun cancelClick(folderId: Long) {
        val action =
            AddExistingRecipesFragmentDirections.actionAddExistingRecipesFragmentToRecipesFragment(
                folderId
            )
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}