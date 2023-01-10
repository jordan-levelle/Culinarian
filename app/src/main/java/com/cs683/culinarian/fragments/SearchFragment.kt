package com.cs683.culinarian.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.SearchAdapter
import com.cs683.culinarian.databinding.FragmentSearchBinding
import com.cs683.culinarian.utilities.onQueryTextChanged
import com.cs683.culinarian.viewmodels.RecipeViewModel
import com.cs683.culinarian.viewmodels.SearchViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var recipeViewModel: RecipeViewModel
    private lateinit var myAdapter : SearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)


//        val searchAdapter = SearchAdapter(searchViewModel::recipeCheckClicked)
//        val checkAdapter = CheckedRecipeAdapter(searchViewModel::recipeUncheckClicked)

        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

//        binding.recipeList.setOnClickListener{ recipeId -> onRecipeClick(recipeId)}

        binding.apply {
            recipeList.apply {
                myAdapter = SearchAdapter { recipeId ->
                    recipeViewModel.setRecipe(recipeId)
                    val action = SearchFragmentDirections.actionSearchFragmentToRecipeFragment(recipeId)
                    this.findNavController().navigate(action)
                }

                this.adapter = myAdapter

                searchViewModel.recipes.observe(viewLifecycleOwner, Observer {
                    myAdapter.replaceItems(it)

                })

//            checkedRecipeList.adapter = checkAdapter
//            searchViewModel.recipeChecked.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    checkAdapter.addItem(it)
//                    searchAdapter.itemCheck(it)
//                    searchViewModel.checkComplete()
//                }
//            })
//            searchViewModel.recipeUnchecked.observe(viewLifecycleOwner, Observer {
//                it?.let {
//                    checkAdapter.itemUncheck(it)
//                    //searchAdapter.reAddItem(it)
//                    searchViewModel.uncheckComplete()
//                }
//            })
            }
            setHasOptionsMenu(true)
        }
    }

//    private fun onRecipeClick(recipeId: Long) {
//        val action = SearchFragmentDirections.actionSearchFragmentToRecipeFragment(recipeId)
//        this.findNavController().navigate(action)
//    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.menu_fragment_search, menu)

            val searchItem = menu.findItem(R.id.recipe_search)
            val searchView = searchItem.actionView as SearchView

            searchView.onQueryTextChanged {
                searchViewModel.searchQuery.value = it
            }
        }
}





