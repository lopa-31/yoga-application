package com.example.yoga.fragment.yoga_fragments

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.yoga.R
import com.example.yoga.adapter.ClickListener
import com.example.yoga.adapter.YogaAsanasListAdapter
import com.example.yoga.model.Pose
import com.example.yoga.viewmodels.YogaAsanaViewModel
import kotlinx.android.synthetic.main.fragment_yoga_asana.*


class YogaAsanaFragment : Fragment(), ClickListener, AdapterView.OnItemSelectedListener {

    private val mYogaAsanaVM by lazy {
        ViewModelProvider(requireActivity()).get(YogaAsanaViewModel::class.java)
    }

    private val adapter by lazy {
        YogaAsanasListAdapter(mYogaAsanaVM.filteredPoses, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_yoga_asana, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list_yoga_asana.adapter = adapter
        list_yoga_asana.setHasFixedSize(true)

        mYogaAsanaVM.selectedDifficulty.observe(viewLifecycleOwner) {
            mYogaAsanaVM.filteredPoses.clear()
            if (it != "All") {
                for (item in mYogaAsanaVM.sortedPoses) {

                    if (item.difficulty == mYogaAsanaVM.selectedDifficulty.value) {
                        mYogaAsanaVM.filteredPoses.add(item)
                    }
                }
                adapter.filterList(mYogaAsanaVM.filteredPoses)
            } else {
                mYogaAsanaVM.filteredPoses.addAll(mYogaAsanaVM.sortedPoses)
                adapter.filterList(mYogaAsanaVM.filteredPoses)
            }
        }
    }

    override fun onClickYogaLI(pose: Pose) {
        val action = YogaAsanaFragmentDirections
            .actionYogaAsanaFragmentToYogaAsanaDetailFragment(pose)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val spinnerView: Spinner = menu.getItem(0).actionView as Spinner

        ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.category_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerView.adapter = it
        }

        spinnerView.onItemSelectedListener = this

        val searchView: SearchView = menu.getItem(1).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filter(it) }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
    }

    private fun filter(text: String) {
        val filteredList: MutableList<Pose> = mutableListOf()

        mYogaAsanaVM.filteredPoses.forEach {
            if (it.sanskrit_name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(it)
            } else if (it.pose_name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(it)
            }
        }
        adapter.filterList(filteredList)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)?.toString()?.let {
            mYogaAsanaVM.setSelectedDifficulty(it)
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}