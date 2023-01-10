package com.cs683.culinarian.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.FolderRecyclerViewAdapter
import com.cs683.culinarian.databinding.FragmentRecipeBinding
import com.cs683.culinarian.databinding.FragmentRecipeFolderRelationBinding
import com.cs683.culinarian.viewmodels.RecipeFolderViewModel
import com.cs683.culinarian.viewmodels.RecipeViewModel

class RecipeFolderRelationFragment : Fragment() {
    private var _binding: FragmentRecipeFolderRelationBinding? = null
    private val binding get() = _binding!!

    private lateinit var foldersViewModel: RecipeFolderViewModel
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeFolderRelationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        foldersViewModel = ViewModelProvider(this).get(RecipeFolderViewModel::class.java)
        foldersViewModel.setRecipe(recipeViewModel.recipeId.value!!)

        binding.viewModel = foldersViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val relationsAdapter = FolderRecyclerViewAdapter(foldersViewModel::removeFromFolder)
        val foldersAdapter = FolderRecyclerViewAdapter(foldersViewModel::addToFolder)

        binding.relationsList.adapter = relationsAdapter
        binding.foldersList.adapter = foldersAdapter

        foldersViewModel.recipeFolders.observe(viewLifecycleOwner, Observer { relations ->
            relations?.let {
                relationsAdapter.replaceItems(relations.folder)
            }
        })

        foldersViewModel.allFolders.observe(viewLifecycleOwner, Observer { folders ->
            folders?.let {
                foldersAdapter.replaceItems(folders)
            }
        })

        foldersViewModel.relationsModified.observe(viewLifecycleOwner, Observer { modified ->
            if (modified) {
                relationsAdapter.replaceItems(foldersViewModel.recipeFolders.value!!.folder)
                foldersAdapter.replaceItems(foldersViewModel.allFolders.value!!)
                foldersViewModel.onRelationsUpdated()
            }
        })

        foldersViewModel.navigateToRecipe.observe(viewLifecycleOwner, Observer { navigate ->
            navigate?.let {
                view.findNavController().navigate(R.id.action_recipeFolderRelationFragment_pop)
                foldersViewModel.onRecipeNavigated()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}