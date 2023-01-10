package com.cs683.culinarian.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.RecipesListViewAdapter
import com.cs683.culinarian.databinding.FragmentRecipesListViewBinding
import com.cs683.culinarian.viewmodels.RecipeViewModel
import com.cs683.culinarian.viewmodels.RecipesListViewModel



class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var addRecipe: Button
    private lateinit var addExistingRecipe: Button
    private lateinit var listViewModel: RecipesListViewModel
    private lateinit var myAdapter: RecipesListViewAdapter
    private lateinit var recipeViewModel: RecipeViewModel

    private var folderId = 0L

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        folderId = RecipesListFragmentArgs.fromBundle(requireArguments()).folderId

        listViewModel = ViewModelProvider(requireActivity()).get(RecipesListViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

        listViewModel.setCurFolder(folderId.toInt())

        binding.recipeList.apply {
            myAdapter = RecipesListViewAdapter { recipeId -> recipeViewModel.setRecipe(recipeId)
                val action = RecipesListFragmentDirections.actionRecipesFragmentToRecipeFragment(recipeId)
                this.findNavController().navigate(action)
            }

            this.adapter = myAdapter

            listViewModel._recipes.observe(viewLifecycleOwner, Observer {
                it?.let {
                    myAdapter.replaceRecipes(it)
                }
            })

            listViewModel.folderId.observe(viewLifecycleOwner, Observer { id ->
                id?.let {
                    if (id == 0) {
                        binding.addExistingRecipeFAB.isClickable = false
                        binding.addExistingRecipeFAB.setBackgroundColor(ContextCompat.getColor(context, R.color.grey)
                        )
                    }
                }
            })

            ItemTouchHelper(SwipeToDeleteCallback()).attachToRecyclerView(this)
        }

        addRecipe = binding.recipesListFloatingActionButton
        addRecipe.setOnClickListener { fabRecipeAddNew() }

        addExistingRecipe = binding.addExistingRecipeFAB
        addExistingRecipe.setOnClickListener { fabAddRecipes(folderId)}
    }


    private fun fabRecipeAddNew () {
        val inflater = LayoutInflater.from(requireContext())
        val v = inflater.inflate(R.layout.add_recipe, null)
        val recipeName = v.findViewById<EditText>(R.id.recipe_name)
        val addDialog = AlertDialog.Builder(requireActivity())
        addDialog.setView(v)
        addDialog.setPositiveButton("OK", Listener(recipeName, this))
        addDialog.setNegativeButton("Cancel") {
            dialogInterface, i ->  dialogInterface.cancel()
        }
        addDialog.create().show()
    }

    private fun fabAddRecipes (folderId : Long) {
        val action = RecipesListFragmentDirections.actionRecipesFragmentToAddExistingRecipesFragment(folderId)
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val recipe = myAdapter.getRecipe(position)
            listViewModel.delRecipeFromFolder(recipe)
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) = makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_SWIPE,
            ItemTouchHelper.RIGHT
        )
    }

    companion object {
        const val ARG_COLUMN_COUNT = "col-count"

        @JvmStatic
        fun newInstance(colCount: Int) =
            RecipesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, colCount)
                }
            }
    }

    /*
    * Add new Recipe
    */
    inner class Listener(val nameView: EditText, val fragment: RecipesListFragment) : DialogInterface.OnClickListener {
        override fun onClick(p0: DialogInterface?, p1: Int) {
            recipeViewModel.setRecipeWithFolder(nameView.text.toString(), folderId.toInt())
            val action = RecipesListFragmentDirections.actionRecipesFragmentToRecipeAddFragment()
            fragment.findNavController().navigate(action)
        }
    }
}




