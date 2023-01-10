package com.cs683.culinarian.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cs683.culinarian.R
import com.cs683.culinarian.adapters.FolderRecyclerViewAdapter
import com.cs683.culinarian.databinding.FragmentFolderRecyclerViewBinding
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.model.Recipe
import com.cs683.culinarian.viewmodels.FolderListViewModel
import com.cs683.culinarian.viewmodels.RecipesListViewModel


/*
* Fragment with a list of Folders
*/
class FolderRecyclerViewFragment : Fragment() {

    private var _binding: FragmentFolderRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var myAdapter: FolderRecyclerViewAdapter
    private lateinit var listViewModel:FolderListViewModel
    private lateinit var recipesListViewModel: RecipesListViewModel


    //Main callback function->returns a view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // inflate xml file fragment_folder_recycler_view
        _binding = FragmentFolderRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listViewModel = ViewModelProvider(this).get(FolderListViewModel::class.java)
        recipesListViewModel = ViewModelProvider(this).get(RecipesListViewModel::class.java)

        binding.allRecipesFolder.setOnClickListener{ allRecipesClick() }

        binding.folderList.apply {
            myAdapter = FolderRecyclerViewAdapter{folder -> folderClick(folder)}

            this.adapter = myAdapter

            listViewModel.folderList.observe(viewLifecycleOwner, Observer {
                myAdapter.replaceItems(it)
            })

            ItemTouchHelper(SwipeToDeleteCallback()).attachToRecyclerView(this)

            binding.fab.setOnClickListener{fabClick()}
        }
    }

    private fun allRecipesClick() {
        val action = FolderRecyclerViewFragmentDirections.actionFoldersFragmentToRecipesFragment(0)
        this.findNavController().navigate(action)

    }

    private fun fabClick (){
        binding.root.findNavController().navigate(R.id.action_foldersFragment_to_addFolderFragment)
    }

    private fun folderClick (folder: Folder) {
        val action = FolderRecyclerViewFragmentDirections.actionFoldersFragmentToRecipesFragment(folder.folderId.toLong())
        this.findNavController().navigate(action)
    }


    inner class SwipeToDeleteCallback: ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.bindingAdapterPosition
            val folder = myAdapter.getFolder(position)
            listViewModel.deleteFolder(folder)
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)
        = makeMovementFlags(
            ItemTouchHelper.ACTION_STATE_SWIPE,
            ItemTouchHelper.RIGHT
        )
    }

    companion object {
        const val ARG_COLUMN_COUNT = "col-count"
        @JvmStatic
        fun newInstance(colCount: Int) =
            FolderRecyclerViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, colCount)
                }

        }
    }
}