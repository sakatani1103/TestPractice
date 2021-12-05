package com.example.testpractice.list

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testpractice.R
import com.example.testpractice.databinding.FragmentListBinding
import com.example.testpractice.others.Status

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding!!

    private val viewModel by viewModels<ListViewModel>()
    private lateinit var listAdapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupRecyclerView()
        subscribeToObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_filter -> {
                showPopUpMenu()
                true
            }
            else -> false
        }
    }

    private fun showPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_menu, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.someday -> viewModel.setFilter(FilterType.SOMEDAY)
                    R.id.has_been_to -> viewModel.setFilter(FilterType.GONE)
                    else -> viewModel.setFilter(FilterType.ALL)
                }
                true
            }
            show()
        }
    }

    private fun subscribeToObserver() {
        viewModel.places.observe(viewLifecycleOwner, { list ->
            listAdapter.places = list
            }
        )
        viewModel.navigateToEdit.observe(viewLifecycleOwner, { it?.let {
            val type = it["type"].toString()
            if (type == "add") { this.findNavController().navigate(ListFragmentDirections.actionListFragmentToEditFragment(null))}
            else { this.findNavController().navigate(ListFragmentDirections.actionListFragmentToEditFragment(it["id"]))}
            viewModel.doneNavigateToEdit()
        } })
    }

    private fun setupRecyclerView() {
        listAdapter = ListAdapter(PlaceListListener { id ->  viewModel.navigateToEdit(id)})
        binding.listRv.apply {
            adapter = listAdapter
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}