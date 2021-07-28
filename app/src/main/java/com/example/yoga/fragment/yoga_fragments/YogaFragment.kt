package com.example.yoga.fragment.yoga_fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yoga.R
import com.example.yoga.databinding.FragmentYogaBinding

class YogaFragment : Fragment() {
    private val binding by lazy {
        FragmentYogaBinding.inflate(layoutInflater)
    }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        binding.practiceYogaAsana.setOnClickListener {
            findNavController().navigate(R.id.action_yogaFragment_to_yogaAsanaFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }
}