package com.example.yoga.fragment.yoga_fragments

import android.os.Bundle
import android.view.*
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


class YogaAsanaFragment : Fragment(), ClickListener {

    private val mYogaAsanaVM by lazy {
        ViewModelProvider(requireActivity()).get(YogaAsanaViewModel::class.java)
    }

    private val adapter by lazy {
        YogaAsanasListAdapter(mYogaAsanaVM.poses, this)
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
    }

    override fun onClickYogaLI(pose: Pose) {
        val action = YogaAsanaFragmentDirections
            .actionYogaAsanaFragmentToYogaAsanaDetailFragment(pose)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {

            val searchView: SearchView = item.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    filter(newText)
                    return false
                }
            })
        }
        return true
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: MutableList<Pose> = mutableListOf()

        // running a for loop to compare elements.
        for (item in mYogaAsanaVM.poses) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.sanskrit_name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            } else if (item.pose_name.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        adapter.filterList(filteredlist)
    }
}