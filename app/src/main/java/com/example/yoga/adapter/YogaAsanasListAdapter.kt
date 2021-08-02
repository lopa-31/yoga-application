package com.example.yoga.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yoga.R
import com.example.yoga.model.Pose
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.list_item_yoga_asana.view.*

class YogaAsanasListAdapter(
    private var poses: List<Pose>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<YogaAsanasListAdapter.ViewHolder>() {

    private var filteredPoses: List<Pose> = poses

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        private val poseName: TextView
        private val sanskritName: TextView
        private val cardLayout: MaterialCardView

        init {
            view.apply {
                poseName = pose_name
                sanskritName = sanskrit_name
                cardLayout = card_layout
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindData(pose: Pose, position: Int) {
            poseName.text = pose.pose_name
            sanskritName.text = pose.sanskrit_name

            cardLayout.setOnClickListener {
                clickListener.onClickYogaLI(pose)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_yoga_asana, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(filteredPoses[position], position)
    }

    override fun getItemCount(): Int {
        return filteredPoses.size
    }

    fun filterList(filteredList: List<Pose>) {
        filteredPoses = filteredList
        notifyDataSetChanged()
    }
}

interface ClickListener{
    fun onClickYogaLI(pose: Pose)
}