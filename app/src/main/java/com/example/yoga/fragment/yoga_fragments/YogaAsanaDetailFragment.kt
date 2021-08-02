package com.example.yoga.fragment.yoga_fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yoga.R
import com.example.yoga.data.poses
import com.example.yoga.model.Pose
import kotlinx.android.synthetic.main.fragment_yoga_asana_detail.*

class YogaAsanaDetailFragment : Fragment() {

    private val pose by lazy {
        requireArguments().getParcelable<Pose>("pose")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_yoga_asana_detail, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pose?.let {
            heading_sanskrit_name.text = it.sanskrit_name
            subheading_pose_name.text = it.pose_name
            difficulty.text = "For ${it.difficulty}s"
            benefits.text = it.benefits

            if (it.description.size == 3){
                val firstDescription = findDescription(it.description[1])
                description.text = firstDescription + it.description[2]
            }
            else{
                description.text = it.description[0]
            }
        }
    }

    private fun findDescription(ref: String): String{
        val poses = poses(requireContext())
        poses.forEach {
            if (it.file_reference == ref)
                return if(it.description.size == 3){
                    findDescription(it.description[1])
                } else
                    it.description[0]
        }
        val refA = ref.split(".")
        return "From ${refA[0]} position"
    }
}