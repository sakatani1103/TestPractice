package com.example.testpractice.edit

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.testpractice.databinding.FragmentEditBinding
import com.example.testpractice.others.EventObserver
import com.example.testpractice.others.Status

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding: FragmentEditBinding
        get() = _binding!!

    private val args: EditFragmentArgs by navArgs()

    private val viewModel by viewModels<EditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.start(args.id)
        binding.etTitle.setOnKeyListener { _, i, keyEvent ->
            return@setOnKeyListener setKeyBoard(view, i, keyEvent) }
        binding.etComment.setOnKeyListener { _, i, keyEvent ->
            return@setOnKeyListener setKeyBoard(view, i, keyEvent)  }
        subscribeToObserve()
    }

    private fun setKeyBoard(view: View, i: Int, keyEvent: KeyEvent): Boolean{
        if((keyEvent.action == KeyEvent.ACTION_UP && i == KeyEvent.KEYCODE_ENTER)||
            (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK)) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
            return true
        }
        return false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeToObserve(){
        viewModel.insertAndUpdateStatus.observe(viewLifecycleOwner, EventObserver { result ->
            if (result.status == Status.SUCCESS){
                this.findNavController().popBackStack()
            }
        })
    }


}