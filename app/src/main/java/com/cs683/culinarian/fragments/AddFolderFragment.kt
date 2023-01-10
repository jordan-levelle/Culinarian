package com.cs683.culinarian.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cs683.culinarian.R
import com.cs683.culinarian.databinding.FragmentAddFolderBinding
import com.cs683.culinarian.model.Folder
import com.cs683.culinarian.viewmodels.RecipesListViewModel
import com.cs683.culinarian.viewmodels.FolderListViewModel

class AddFolderFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentAddFolderBinding? = null
    private val binding get() = _binding!!

    private lateinit var listViewModel: FolderListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel = ViewModelProvider(requireActivity()).get(FolderListViewModel::class.java)

        binding.addFolderFab.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.addFolderFab) {
            val folder = Folder(
                0, binding.addFolderEdit.text.toString()
            )
            listViewModel.addFolder(folder)
            //viewModel.setCurFolder(folder)
        }
        view.findNavController().navigate(R.id.action_addFolderFragment_pop)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
